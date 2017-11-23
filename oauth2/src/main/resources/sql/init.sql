insert into user(id, username, password, created_date, created_user)
values (1, 'test', '$2a$10$2SLqTL7.Ug9cyfRPFwxQeemMA4SeB9MymLjRl4RGR0h7aHwLDy7qC', now(), 1);

insert into oauth2_client (id, name, secret, created_date, created_user)
values ('b98e21b4-ce2a-11e7-abc4-cec278b6b50a', 'OAuth2 Test Application', '$2a$10$2SLqTL7.Ug9cyfRPFwxQeemMA4SeB9MymLjRl4RGR0h7aHwLDy7qC', now(), 1);

insert into oauth2_allow_domain (id, domain_name, client_id, created_date, created_user)
values (1, 'http://localhost', 'b98e21b4-ce2a-11e7-abc4-cec278b6b50a', now(), 1);

insert into oauth2_scope (id, description, created_date, created_user)
values ('user:public_profile', 'Only public information of user', now(), 1);

insert into oauth2_client_scope (client_id, scope, created_date, created_user)
values ('b98e21b4-ce2a-11e7-abc4-cec278b6b50a', 'user:public_profile', now(), 1);

insert into sequence(sequence_name, next_val)
values ('user', 1);

insert into sequence(sequence_name, next_val)
values ('oauth2_client', 1);

insert into sequence(sequence_name, next_val)
values ('oauth2_allow_domain', 1);