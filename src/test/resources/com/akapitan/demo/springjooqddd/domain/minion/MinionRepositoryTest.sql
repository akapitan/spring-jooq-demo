BEGIN;
DO
$$
    DECLARE

        minion1 UUID       := '00000001-0000-0000-0000-b00000000000';
        evil_master_id UUID= '00000001-0000-0000-0000-a00000000000';

    BEGIN

        insert into person(id, name, lastname) values (evil_master_id, 'Felonius', 'Gru');
        insert into minion(id, version, name, number_of_eyes, evil_master, description)
        VALUES (minion1, 1, 'Aco', 2, evil_master_id, '{
          "apperance": {
            "Hair": "none",
            "Eyes": "2"
          },
          "personality": {
            "Hair": "none",
            "Eyes": "2"
          }
        }');
        insert into minion(id, version, name, number_of_eyes, evil_master, description)
        VALUES (uuid_generate_v4(), 1, 'Maco', 1, evil_master_id, '{
          "apperance": {
            "Hair": "none",
            "Eyes": "2"
          },
          "personality": {
            "Hair": "none",
            "Eyes": "2"
          }
        }');
        insert into minion(id, version, name, number_of_eyes, evil_master, description)
        VALUES (uuid_generate_v4(), 1, 'Kaco', 2, evil_master_id, '{
          "apperance": {
            "Hair": "none",
            "Eyes": "2"
          },
          "personality": {
            "Hair": "none",
            "Eyes": "2"
          }
        }');

        insert into toy(minion, name, material)
        VALUES (minion1, 'Pistol', 'metal'),
               (minion1, 'Ball', 'rubber'),
               (minion1, 'Song', 'air');

        insert into color(minion, name)
        values (minion1, 'red'),
               (minion1, 'green'),
               (minion1, 'blue');
    END
$$;
