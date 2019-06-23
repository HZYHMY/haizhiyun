package com.haizhiyun.controller;

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

import com.alibaba.fastjson.JSONObject;
import com.haizhiyun.annotation.UserLoginToken;
import com.haizhiyun.entity.bean.Permission;
import com.haizhiyun.exception.ErrorCode;
import com.haizhiyun.exception.MyException;
import com.haizhiyun.service.PermissionService;
import com.haizhiyun.util.UuidUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description = "权限")
@RestController
@RequestMapping(value = "/permission")
public class PermissionController {

	private static Logger logger = LoggerFactory.getLogger(PermissionController.class);

	@Autowired
	private PermissionService permissionService;
	/**
	 * 创建权限
	 * @param permissionName
	 * @param url
	 * @param father
	 * @param pid
	 * @return
	 */
	@ApiOperation(notes = "创建权限",value = "创建权限")
	@UserLoginToken
	@ApiImplicitParams({
		@ApiImplicitParam(name = "permissionName",required = true,paramType = "create",value = "权限名",dataType = "String"),
		@ApiImplicitParam(name = "url",required = true,paramType = "create",value = "url",dataType = "String"),
		@ApiImplicitParam(name = "father",required = false,paramType = "create",value = "是否父权限",dataType = "String"),
		@ApiImplicitParam(name = "pid",required = false,paramType = "create",value = "父权限id",dataType = "Long")
	})
	@RequiresPermissions("/permission/createPermission")
	@PostMapping(value = "/createPermission")
	public JSONObject createPermission(@RequestParam(name = "permissionName",required = true) String permissionName,@RequestParam(name = "url",required = true) String url,
			@RequestParam(name = "father",required = false) String father,@RequestParam(name = "pid",required = false) Long pid) {
		logger.debug("---------------创建权限---------------------");
		//权限是否存在
		Permission checkUrlPermission = permissionService.selectPermission(new Permission(url));
		if (!ObjectUtils.isEmpty(checkUrlPermission)) {
			throw new MyException(ErrorCode.PARAMETER, "权限url已存在");
		}
		Permission permission = new Permission();
		permission.setUuid(UuidUtil.getUuid());
		permission.setPermissionName(permissionName);
		if (!ObjectUtils.isEmpty(father)) {
			permission.setFather(father);
			Permission newPermission = permissionService.selectPermission(permission);
			if (!ObjectUtils.isEmpty(newPermission)) {
				throw new MyException(ErrorCode.PARAMETER, "父权限名已存在");
			}
			if (ObjectUtils.isEmpty(pid)) {
				permission.setPid(0L);
			}
		}
		if (!ObjectUtils.isEmpty(pid)) {
			permission.setPid(pid);
		}
		permission.setUrl(url);
		//创建权限
		int i = permissionService.createPermission(permission);
		if (i < 0) {
			throw new MyException(ErrorCode.PARAMETER, "权限创建失败");
		}
		JSONObject permissionResult = new JSONObject();
		permissionResult.put("message", "SUCCESS");
		return permissionResult;
	}
	@ApiOperation(notes = "修改权限",value = "修改权限")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "uuid",required = true,paramType = "update",value = "uuid",dataType = "String"),
		@ApiImplicitParam(name = "permissionName",required = false,paramType = "update",value = "权限名",dataType = "String"),
		@ApiImplicitParam(name = "url",required = false,paramType = "update",value = "url",dataType = "String"),
		@ApiImplicitParam(name = "father",required = false,paramType = "update",value = "是否父权限",dataType = "String"),
		@ApiImplicitParam(name = "pid",required = false,paramType = "update",value = "父权限id",dataType = "Long")
	})
	@UserLoginToken
	@RequiresPermissions("/permission/updatePermission")
	@PostMapping(value = "/updatePermission")
	public JSONObject updatePermission(@RequestParam(name = "uuid",required = true) String uuid,@RequestParam(name = "permissionName",required = false) String permissionName,@RequestParam(name = "url",required = false) String url,
			@RequestParam(name = "father",required = false) String father,@RequestParam(name = "pid",required = false) Long pid) {
		logger.debug("---------------修改权限---------------------");
		//权限是否存在
		Permission permission = new Permission();
		permission.setUuid(uuid);
		Permission newPermissions = permissionService.selectPermission(permission);
		Permission p = new Permission();
		//当url发生变化时，判断是否存在
		if (!ObjectUtils.isEmpty(url)&&!newPermissions.getUrl().equals(url)) {
			p .setUrl(url);
			Permission selectPermission = permissionService.selectPermission(p);
			if (!ObjectUtils.isEmpty(selectPermission)) {
				throw new MyException(ErrorCode.PARAMETER, "url已存在");
			}
			permission.setUrl(url);
		}
		if (!ObjectUtils.isEmpty(permissionName)&&!newPermissions.getPermissionName().equals(permissionName)) {
			p.setPermissionName(permissionName);
			permission.setPermissionName(permissionName);
		}
		if (!ObjectUtils.isEmpty(father)) {
			p.setFather(father);
			Permission newPermission = permissionService.selectPermission(p);
			if (!ObjectUtils.isEmpty(newPermission)) {
				throw new MyException(ErrorCode.PARAMETER, "父权限已存在");
			}
			permission.setFather(father);
			if (ObjectUtils.isEmpty(pid)) {
				permission.setPid(0L);
			}
		} 
		if (!ObjectUtils.isEmpty(pid)) {
			permission.setPid(pid);
		}
		//创建权限
		int i = permissionService.updatePermission(permission);
		if (i <= 0) {
			throw new MyException(ErrorCode.PARAMETER, "权限修改失败");
		}
		JSONObject permissionResult = new JSONObject();
		permissionResult.put("message", "SUCCESS");
		return permissionResult;
	}
	@ApiOperation(notes = "查询所有权限",value = "查询所有权限")
	@UserLoginToken
	@RequiresPermissions(value = "/permission/getAllPermission")
	@PostMapping(value = "/getAllPermission")
	public JSONObject getAllPermission() {
		List<Permission> selectPermissionList = permissionService.selectPermissionList(null);
		JSONObject permissionResult = new JSONObject();
		permissionResult.put("PermissionList", selectPermissionList);
		permissionResult.put("message", "SUCCESS");
		return permissionResult;

	}
	@ApiOperation(notes = "删除权限",value = "删除权限")
	@ApiImplicitParams(@ApiImplicitParam(name = "uuids",required = true,paramType = "delete",value = "uuids",dataType = "String"))
	@UserLoginToken
	@RequiresPermissions(value = "/permission/deletePermission")
	@PostMapping(value = "/deletePermission")
	public JSONObject deletePermission(@RequestParam(name = "uuids" , required = true) List<String> uuids) {
		for (String uuid : uuids) {
			int i = permissionService.deletePermission(uuid);

			if (i <= 0) {
				throw new MyException(ErrorCode.PARAMETER, "权限删除失败");
			};
		}
		JSONObject permissionResult = new JSONObject();
		permissionResult.put("message", "SUCCESS");
		return permissionResult;

	}


}
