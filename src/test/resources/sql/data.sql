insert into feature(id, title, description, status)
values (4, 'testFeature4', 'test4', 'OPEN');
insert into feature(id, title, description, status)
values (5, 'testFeature5', 'test5', 'OPEN');
insert into feature(id, title, description, status)
values (6, 'testFeature6', 'test6', 'OPEN');

insert into task(id, title, description, feature_id, current_user_id, status)
values (5, 'testTask', 'test', 4, 2, 'OPEN');
insert into task(id, title, description, feature_id, current_user_id, status)
values (6, 'testTask', 'test', 4, 5, 'IN_PROGRESS');
insert into task(id, title, description, feature_id, current_user_id, status)
values (7, 'testTask', 'test', 4, 5, 'RESOLVED');
insert into task(id, title, description, feature_id, current_user_id, status)
values (8, 'testTask', 'test', 4, 8, 'RESOLVED');

insert into task(id, title, description, feature_id, current_user_id, status)
values (9, 'testTask', 'test', 5, 9, 'COMPLETED');
insert into task(id, title, description, feature_id, current_user_id, status)
values (10, 'testTask', 'test', 5, 9, 'COMPLETED');

insert into task(id, title, description, feature_id, current_user_id, status)
values (11, 'testTask', 'test', 6, 10, 'RESOLVED');
insert into task(id, title, description, feature_id, current_user_id, status)
values (12, 'testTask', 'test', 6, 6, 'IN_PROGRESS');
insert into task(id, title, description, feature_id, current_user_id, status)
values (13, 'testTask', 'test', 6, 6, 'IN_PROGRESS');

insert into bug(id, title, description, status, task_id)
values (1, 'testBug', 'test', 'OPEN', 12);

insert into user_feature(user_id, feature_id)
values (3, 5);
insert into user_feature(user_id, feature_id)
values (6, 5);
insert into user_feature(user_id, feature_id)
values (9, 5);

insert into user_feature(user_id, feature_id)
values (2, 4);
insert into user_feature(user_id, feature_id)
values (5, 4);
insert into user_feature(user_id, feature_id)
values (8, 4);

insert into user_feature(user_id, feature_id)
values (4, 6);
insert into user_feature(user_id, feature_id)
values (7, 6);
insert into user_feature(user_id, feature_id)
values (10, 6);

select setval('feature_id_seq', (select max(id) from feature));
select setval('task_id_seq', (select max(id) from task));
select setval('bug_id_seq', (select max(id) from bug));