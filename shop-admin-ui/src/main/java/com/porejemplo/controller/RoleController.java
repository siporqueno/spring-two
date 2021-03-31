package com.porejemplo.controller;

import com.porejemplo.persist.model.Role;
import com.porejemplo.persist.repo.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/role")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    private final RoleRepository roleRepository;

    @Autowired
    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String listPage(Model model) {
        logger.info("List page for roles requested.");

        model.addAttribute("roles", roleRepository.findAll());
        return "role";
    }

    @GetMapping("/{id}")
    public String editPage(@PathVariable("id") Long id, Model model) {
        logger.info("Edit page for role with id {} requested", id);

        model.addAttribute("role", roleRepository.findById(id)
                .orElseThrow(NotFoundException::new));
        return "role_form";
    }

    @GetMapping("/new")
    public String create(Model model) {
        logger.info("new endpoint requested for role");

        model.addAttribute("role", new Role());
        return "role_form";
    }

    @PostMapping("/update")
    public String update(Role role, Model model) {
        logger.info("update endpoint requested for role");

        logger.info("Updating role with id {}", role.getId());
        Role savedRole = roleRepository.save(role);
        if (role.getId() == null) {
            role.setId(savedRole.getId());
        }
        return "redirect:/role";
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") Long id) {
        logger.info("Delete endpoint requested for role with id {}", id);
        roleRepository.deleteById(id);
        return "redirect:/role";
    }
}
