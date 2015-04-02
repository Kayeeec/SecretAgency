CREATE TABLE agents (
    id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    codename VARCHAR(250),
    contact VARCHAR(250),
    note VARCHAR(250),
    status VARCHAR(10)
);

CREATE TABLE missions (
  id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY ,
  missionname VARCHAR(250),
  location VARCHAR(250),
  startTime DATE,
  endTime DATE,
  maxEndTime DATE,
  description VARCHAR(250),
  status VARCHAR(10)
)

CREATE TABLE assignments (
  assignmentId INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY ,
  agentId INTEGER NOT NULL REFERENCES agents(ID),
  missionId INTEGER NOT NULL REFERENCES missions(ID),
  payment DOUBLE,
  startDate DATE,
  endDate DATE,
)