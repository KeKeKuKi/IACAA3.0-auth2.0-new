SELECT
       tr.id AS id,
       tr.resource_server_id AS resource_server_id,
       tr.resource_server_name AS resource_server_name,
       tr.scope AS scope,
       tr.name AS  name
FROM tb_resource tr
WHERE tr.id in (SELECT
                    tp.resource_id AS rid
                FROM tb_permission tp
                WHERE tp.id in (SELECT
                                    trp.permission_id AS pid
                                FROM tb_role_permission trp
                                where role_id =
                                      (SELECT
                                           tur.role_id
                                       FROM tb_user_role tur
                                       WHERE tur.user_id = :userId)))
