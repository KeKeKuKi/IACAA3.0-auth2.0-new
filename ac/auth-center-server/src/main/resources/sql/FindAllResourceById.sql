SELECT TR.id                   AS id,
       TR.resource_server_id   AS resource_server_id,
       TR.resource_server_name AS resource_server_name,
       TR.scope                AS scope,
       TR.name                 AS name
FROM tb_resource AS TR WHERE TR.id = :id