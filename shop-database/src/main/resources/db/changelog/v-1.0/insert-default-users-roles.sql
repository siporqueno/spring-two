INSERT INTO `users` (`login`, `password`)
    VALUE ('admin', '$2a$10$r00TpQblhs8pXjAxcyVts.k9uKF2Qg8D4HkK4yyghcL6tWE0u4v5G'),
        ('guest', '$2a$10$x8/fnimmWWzU1H5CNnL9oOG8Y/3cBWf2d3tnT8TJqIRmXCmuPRFBW'),
        ('superadmin', '$2a$10$p.FavTMz7yk0lfWeEk.rTOmqPmF8hvij9juYn1Hjw6twWAGLG1Eey');
GO

INSERT INTO `roles` (`name`)
VALUE ('ROLE_ADMIN'), ('ROLE_GUEST'), ('ROLE_SUPER_ADMIN');
GO

INSERT INTO `users_roles`(`user_id`, `role_id`)
SELECT (SELECT id FROM `users` WHERE `login` = 'admin'), (SELECT id FROM `roles` WHERE `name` = 'ROLE_ADMIN')
UNION ALL
SELECT (SELECT id FROM `users` WHERE `login` = 'guest'), (SELECT id FROM `roles` WHERE `name` = 'ROLE_GUEST')
UNION ALL
SELECT (SELECT id FROM `users` WHERE `login` = 'superadmin'), (SELECT id FROM `roles` WHERE `name` = 'ROLE_SUPER_ADMIN');
GO