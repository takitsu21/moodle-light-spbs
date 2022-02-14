FROM postgres:14.1
ADD scripts/db_courses_init.sql /docker-entrypoint-initdb.d
RUN chmod a+r /docker-entrypoint-initdb.d/*
EXPOSE 5434