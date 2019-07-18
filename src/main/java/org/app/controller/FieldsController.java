package org.app.controller;

import org.app.entity.*;
import org.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RequestMapping("/rest/fields")
@RestController
public class FieldsController extends  AbstractController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFieldRepository userFieldRepository;

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private FieldPermissionRepository fieldPermissionRepository;

    @Autowired
    private RoleRepository roleRepository;


    @PreAuthorize("hasAnyRole('USER','ADMIN', 'EDITOR')")
    @RequestMapping(value = "/{dataId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserData fieldByDataId(@AuthenticationPrincipal UserDetails user, @PathVariable Long dataId) {
        UserData userData =  userDataRepository.findById(dataId)
                .orElseThrow(() -> new IllegalArgumentException("user data does not exist"));

        UserField one = userData.getField();
        User accnt = userRepository.findByAccountName(user.getUsername()).
                orElseThrow(() -> new IllegalArgumentException("username not found"));;

        List<Role> roles = accnt.getRoles();

        Optional<FieldPermission> found = roles.stream().map(role ->
                fieldPermissionRepository.findByFieldAndRole(one, role)
        ).filter(f -> f.isPresent() && FieldPermission.ACCESS_TYPE.VIEW.equals(f.get().getAccessType()))
                .map(Optional::get)
                .findFirst();

        found.orElseThrow(() -> new AccessDeniedException("not found or access is denied"));
        return userData;
    }


    //@PostFilter("hasPermission(filterObject, 'read')")
    @PostFilter("hasAuthority('USERFIELD_READ_PRIVILEGE')")
    //@PostFilter("hasPermission(returnObject, 'read')")
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserField> allFields(@AuthenticationPrincipal UserDetails user) {
        return userFieldRepository.findAll();
    }

    @PreAuthorize("hasPermission(#userField, 'create')")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserField createField(UserField userField, @AuthenticationPrincipal UserDetails user) {
        return userFieldRepository.getOne(15L);
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    //@PreAuthorize("hasPermission(#noticeMessage, 'WRITE')")
    @RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public FieldPermission createFieldPermission(@AuthenticationPrincipal UserDetails user,
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
