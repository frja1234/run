package com.scy.running.test;

import com.scy.running.model.TbRole;
import com.scy.running.service.ITbRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class RoleServiceTests {

    @Autowired
    private ITbRoleService roleService;

    @Test
    void selectRoleById() {


    }
}
