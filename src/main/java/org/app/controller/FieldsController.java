package org.app.controller;

import org.app.entity.FieldPermission;
import org.app.entity.Role;
import org.app.entity.UserData;
import org.app.entity.UserField;
import org.app.repository.FieldPermissionRepository;
import org.app.repository.RoleRepository;
import org.app.repository.UserDataRepository;
import org.app.repository.UserFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RequestMapping("/rest/fields")
@RestController
public class FieldsController extends  AbstractController {


    @Autowired
    private UserFieldRepository userFieldRepository;

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private FieldPermissionRepository fieldPermissionRepository;

    @Autowired
    private RoleRepository roleRepository;


    @PreAuthorize("hasAnyRole('USER','ADMIN', 'EDITOR')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserField fields(@AuthenticationPrincipal UserDetails user, @PathVariable Long id) {
        UserData userData = userDataRepository.findById(id).get();

        UserField one = userData.getField();

        return one;
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public FieldPermission fieldPermission(@AuthenticationPrincipal UserDetails user,
                                     @PathVariable Long id ,
                                     @RequestParam Long roleId,
                                     @RequestParam FieldPermission.ACCESS_TYPE type) {
        UserField userField = userFieldRepository.findById(id).get();
        Role role = roleRepository.findById(roleId).get();

        FieldPermission fp = new FieldPermission();
        fp.setField(userField);
        fp.setRole(role);
        fp.setAccessType(type);

        FieldPermission save = fieldPermissionRepository.save(fp);

        return save;
    }




}
