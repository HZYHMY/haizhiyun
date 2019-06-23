package com.haizhiyun.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.jsf.FacesContextUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haizhiyun.annotation.UserLoginToken;
import com.haizhiyun.entity.bean.Role;
import com.haizhiyun.entity.bean.RolePermission;
import com.haizhiyun.exception.ErrorCode;
import com.haizhiyun.exception.MyException;
import com.haizhiyun.service.PermissionService;
import com.haizhiyun.service.RolePermissionService;
import com.haizhiyun.service.RoleService;
import com.haizhiyun.util.UuidUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description = "角色")
@RestController
@RequestMapping(value = "/role")
public class RoleController {

	private static Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;
	@Autowired
	private RolePermissionService rolePermissionService;

	/**
	 * 创建角色
	 * @param roleName
	 * @param permissionIdList
	 * @return
	 */
	@ApiOperation(notes = "创建角色赋予权限" ,value= "创建角色赋予权限")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "roleName",required = true,paramType = "create",value = "角色名",dataType = "String"),
		@ApiImplicitParam(name = "permissionIdList" ,required = true,paramType = "select" ,value = "权限id集合" ,dataType = "List")
	})
	@UserLoginToken
	@RequiresPermissions("/role/createRole")
	@PostMapping(value = "/createRole")
	public JSONObject createRole(@RequestParam(name="roleName",required = true) String roleName,@RequestParam(name = "permissionIdList",required = true) List<Long> permissionIdList) {
		logger.debug("--------------创建角色赋予权限--------------------");
		//是否存在
		List<Role> selectRole = roleService.selectRole(new Role(roleName));
		if (!ObjectUtils.isEmpty(selectRole)) {
			Role checkRole = selectRole.get(0);
			if (!ObjectUtils.isEmpty(checkRole)) {
				throw new MyException(ErrorCode.Role, "角色已存在");
			}
		}

		Role role = new Role(UuidUtil.getUuid(), roleName);
		int i = roleService.createRole(role);
		if (i<0) {
			throw new MyException(ErrorCode.Role, "创建角色失败");
		}
		//查询角色id
		List<Role> roleList = roleService.selectRole(role);
		Role newRole = roleList.get(0);
		List<RolePermission> rolePermissionList = new ArrayList<>();
		for (Long permissionId : permissionIdList) {
			RolePermission rolePermission = new RolePermission(UuidUtil.getUuid(), newRole.getId(), permissionId);
			rolePermissionList.add(rolePermission);
		}
		//创建角色权限关系
		int j = rolePermissionService.createRolePermission(rolePermissionList);
		if (j < 0) {
			throw new MyException(ErrorCode.Role, "创建角色失败");
		}
		JSONObject roleResult = new JSONObject();
		roleResult.put("message", "SUCCESS");
		return roleResult;
	}
	/**
	 * 查询所有角色
	 * @return
	 */
	@ApiOperation(notes = "查询所有角色",value = "查询所有角色")
	@UserLoginToken
	@RequiresPermissions("/role/selectRoleList")
	@PostMapping(value = "/selectRoleList")
	public JSONObject selectRoleList() {
		logger.debug("--------------查询所有角色--------------------");
		List<Role> roleList = roleService.selectRole(null);
		if (ObjectUtils.isArray(roleList)) {
			return null;
		}
		JSONObject ruleResult = new JSONObject();
		ruleResult.put("roleList", roleList);
		ruleResult.put("message", "SUCCESS");
		return ruleResult;
	}
	/**
	 * 查询某个角色
	 * @param roleId
	 * @return
	 */
	@ApiOperation(notes = "查询角色",value = "查询角色")
	@ApiImplicitParams(
			@ApiImplicitParam(name = "roleId",required = true,paramType = "select",value = "角色Id",dataType = "Long")
			)
	@UserLoginToken
	@RequiresPermissions("/role/selectRole")
	@PostMapping(value = "/selectRole")
	public JSONObject selectRole(@RequestParam(name = "roleId",required = true) Long roleId) {
		logger.debug("------------查询某个角色--------------------");
		//查询角色
		Role newRole = roleService.selectRoleById(roleId);
		JSONObject ruleResult = new JSONObject();
		ruleResult.put("role", newRole);
		ruleResult.put("message", "SUCCESS");
		return ruleResult;
	}
	/**
	 * 修改角色
	 * @param roleId
	 * @param roleName
	 * @param permissionList
	 * @return
	 */
	@ApiOperation(notes = "修改角色",value = "修改角色")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "roleId",required = true,paramType = "select",value = "角色Id",dataType = "Long"),
		@ApiImplicitParam(name = "roleName",required = false,paramType = "update",value= "角色名字",dataType = "String"),
		@ApiImplicitParam(name = "permissionIdList",required = false,paramType = "select",value = "权限id集合",dataType = "List")
	})
	@UserLoginToken
	@RequiresPermissions("/role/updateRole")
	@PostMapping(value = "/updateRole")
	public JSONObject updateRole(@RequestParam(name = "roleId",required = true) Long roleId,@RequestParam(name = "roleName",required = false) String roleName,@RequestParam(name = "permissionIdList",required = false) List<Long> permissionIdList) {
		logger.debug("--------------修改角色--------------------");
		//查询角色
		Role role = roleService.selectRoleById(roleId);
		if (!ObjectUtils.isEmpty(roleName)) {
			if (!roleName.equals(role.getRoleName())) {
				List<Role> roleList = roleService.selectRole(null);
				if (!ObjectUtils.isEmpty(roleList)) {
					for (Role checkRole : roleList) {
						if (roleName.equals(checkRole.getRoleName())) {
							throw new MyException(ErrorCode.Role, "角色名已存在");
						}
					}
				}
				role.setRoleName(roleName);
				//修改角色
				int i = roleService.updateRole(role);
				if (i < 0) {
					throw new MyException(ErrorCode.Role, "角色修改失败");
				}
			}
		}
		List<RolePermission> rolePermissionList = new ArrayList<>();
		if (!ObjectUtils.isEmpty(permissionIdList)) {
			for (Long permissionId : permissionIdList) {
				RolePermission rolePermission = new RolePermission(UuidUtil.getUuid(), roleId, permissionId);
				rolePermissionList.add(rolePermission);
			}
			//删除与角色相关的权限
			int i = rolePermissionService.deleteRolePermission(roleId);
			if (i < 0) {
				throw new MyException(ErrorCode.Role, "角色权限删除失败");
			}
			//创建角色权限关系
			int j = rolePermissionService.createRolePermission(rolePermissionList);
			if (j < 0) {
				throw new MyException(ErrorCode.Role, "角色绑定权限失败");
			}
		}
		JSONObject ruleResult = new JSONObject();
		ruleResult.put("message", "SUCCESS");
		return ruleResult;
	}
	@ApiOperation(notes = "删除角色",value = "删除角色")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "roleId",required = true,paramType = "select",value = "角色Id",dataType = "Long")
	})
	@UserLoginToken
	@RequiresPermissions("/role/deleteRole")
	@PostMapping(value = "/deleteRole")
	public JSONObject deleteRole(@RequestParam(name = "roleId",required = true) Long roleId) {
		logger.debug("--------------删除角色--------------------");
		//删除角色关系
		int i = rolePermissionService.deleteRolePermission(roleId);
		if (i < 0 ) {
			throw new MyException(ErrorCode.Role, "角色解除权限失败");
		}
		//删除角色
		int j = roleService.deleteRole(roleId);
		if (j < 0) {
			throw new MyException(ErrorCode.Role, "角色删除失败");
		}
		JSONObject ruleResult = new JSONObject();
		ruleResult.put("message", "SUCCESS");
		return ruleResult;
	}

}
