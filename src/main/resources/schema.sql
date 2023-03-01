
CREATE TABLE IF NOT EXISTS statuses (
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar (20) not null
);


create TABLE IF NOT EXISTS USERS (
ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
NAME varchar (50) not null,
EMAIL varchar (50) NOT null
);
create unique index if not exists USER_EMAIL_UINDEX on USERS (email);


create table if not exists requests (
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
description varchar (200) not null,
requestor INTEGER not null,
created TIMESTAMP WITHOUT TIME ZONE not null,
FOREIGN KEY (requestor) REFERENCES users (id)
);


create table if not exists items (
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar (50) not null,
description varchar (200) not null,
available  boolean not null,
owner_id INTEGER not null,
request_id INTEGER,
FOREIGN KEY (request_id) REFERENCES requests (id),
FOREIGN KEY (owner_id) REFERENCES users (id)
);



CREATE TABLE IF NOT EXISTS BOOKINGS (
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
start smalldatetime not null,
end smalldatetime not null,
itemId INTEGER not null ,
booker INTEGER not null ,
status integer not null,
FOREIGN KEY (booker) REFERENCES users (id),
FOREIGN KEY (itemId) REFERENCES items (id),
FOREIGN KEY (status) REFERENCES statuses (id)
);


