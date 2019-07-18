

CREATE TABLE IF NOT EXISTS `acl_sid` (

  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `principal` tinyint(1) NOT NULL,

  `sid` varchar(100) NOT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `unique_uk_1` (`sid`,`principal`)

) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;



CREATE TABLE IF NOT EXISTS `acl_class` (

  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `class` varchar(255) NOT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `unique_uk_2` (`class`)

) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;



CREATE TABLE IF NOT EXISTS `acl_entry` (

  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `acl_object_identity` bigint(20) NOT NULL,

  `ace_order` int(11) NOT NULL,

  `sid` bigint(20) NOT NULL,

  `mask` int(11) NOT NULL,

  `granting` tinyint(1) NOT NULL,

  `audit_success` tinyint(1) NOT NULL,

  `audit_failure` tinyint(1) NOT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `unique_uk_4` (`acl_object_identity`,`ace_order`),

  KEY `foreign_fk_5` (`sid`)

) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=43 ;



CREATE TABLE IF NOT EXISTS `acl_object_identity` (

  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `object_id_class` bigint(20) NOT NULL,

  `object_id_identity` bigint(20) NOT NULL,

  `parent_object` bigint(20) DEFAULT NULL,

  `owner_sid` bigint(20) DEFAULT NULL,

  `entries_inheriting` tinyint(1) NOT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `unique_uk_3` (`object_id_class`,`object_id_identity`),

  KEY `foreign_fk_1` (`parent_object`),

  KEY `foreign_fk_3` (`owner_sid`)

) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;




ALTER TABLE `acl_entry`
  ADD CONSTRAINT `foreign_fk_4` FOREIGN KEY (`acl_object_identity`) REFERENCES `acl_object_identity` (`id`),
  ADD CONSTRAINT `foreign_fk_5` FOREIGN KEY (`sid`) REFERENCES `acl_sid` (`id`);


ALTER TABLE `acl_object_identity`
  ADD CONSTRAINT `foreign_fk_1` FOREIGN KEY (`parent_object`) REFERENCES `acl_object_identity` (`id`),
  ADD CONSTRAINT `foreign_fk_2` FOREIGN KEY (`object_id_class`) REFERENCES `acl_class` (`id`),
  ADD CONSTRAINT `foreign_fk_3` FOREIGN KEY (`owner_sid`) REFERENCES `acl_sid` (`id`);


--insert some test settings


INSERT INTO acl_sid (id, principal, sid) VALUES
  (1, 0, 'ROLE_ADMIN'),
  (2, 0, 'ROLE_USER'),
  (3, 0, 'ROLE_EDITOR');

INSERT INTO acl_class (id, class) VALUES (1, 'org.app.entity.UserField');


INSERT INTO acl_object_identity
  (id, object_id_class, object_id_identity,
                parent_object, owner_sid, entries_inheriting)
  VALUES
  (1, 1, 13,        NULL, 3, 0),
  (2, 1, 15,        NULL, 3, 0),
  (3, 1, 17,        NULL, 3, 0);


INSERT INTO acl_entry
  (id, acl_object_identity, ace_order,
                sid, mask, granting, audit_success, audit_failure)
  VALUES
  (1, 1, 1,      1, 1, 1, 1, 1),
  (2, 1, 2,      1, 2, 1, 1, 1),
  (3, 1, 3,      3, 1, 1, 1, 1),
  (4, 2, 1,      2, 1, 1, 1, 1),
  (5, 2, 2,      3, 1, 1, 1, 1),
  (6, 3, 1,      3, 1, 1, 1, 1),
  (7, 3, 2,      3, 2, 1, 1, 1);

