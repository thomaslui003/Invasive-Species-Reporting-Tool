CREATE TABLE userlist (
  user_name TEXT PRIMARY KEY,
  password TEXT
);
CREATE TABLE reportedcases (
  case_id SERIAL PRIMARY KEY,
  species_name TEXT,
  date_reported DATE,
  reporter_name TEXT,
  user_name TEXT REFERENCES userlist(user_name),
  province TEXT,
  coordinates TEXT
);

CREATE TABLE testuserlist (
  user_name TEXT PRIMARY KEY,
  password TEXT                  
);

CREATE TABLE testreportedcases (
  case_id SERIAL PRIMARY KEY,
  species_name TEXT,                                           
  date_reported DATE,
  reporter_name TEXT,
  user_name TEXT REFERENCES testuserlist(user_name),
  province TEXT,
  coordinates TEXT
);

