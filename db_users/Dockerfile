FROM postgres:14.1
ADD scripts/db_users_init.sql.sql /docker-entrypoint-initdb.d
RUN chmod a+r /docker-entrypoint-initdb.d/*
EXPOSE 5435