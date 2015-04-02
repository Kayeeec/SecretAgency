
-- INSERT INTO AGENTS VALUES ('CODENAME', 'CONTACT', 'NOTE', 'STATUS');
-- INSERT INTO MISSIONS VALUES ('MISSIONNAME', 'LOCATION', 'STARTTIME', 'ENDTIME', 'MAXENDTIME', 'DESCRIPTION', 'MSTATUS');
-- INSERT INTO ASSIGNMENTS VALUES ('AGENTID', 'MISSIONID', 'PAYMENT', 'STARTDATE', 'ENDDATE');

-- INSERT INTO AGENTS (CODENAME, CONTACT, NOTE, STATUS) VALUES
-- ('Bond', 'bondJamesBond@mi6.uk', 'Martini. Shaken, not stirred.', 'ACTIVE');
-- INSERT INTO AGENTS VALUES ('BlackWidow', '459 562 118', 'can speak Russian', 'ACTIVE');
--
-- INSERT INTO MISSIONS VALUES ('Cobra', 'Mozambique', '2015-04-16', '2015-05-08', '', 'remember, no russian', 'WAITING');
-- INSERT INTO MISSIONS VALUES ('Mangus', 'Paris', '2015-02-11', '', '', 'will be long', 'ONGOING');
--
-- INSERT INTO ASSIGNMENTS VALUES ('1', '1', '250000', '2015-04-16', '2015-05-08');
-- INSERT INTO ASSIGNMENTS VALUES ('2', '2', '', '2015-02-11', '');

INSERT INTO agents (codename, contact, note, status) VALUES ('agent000', '000@secretmail.org', 'extremely dangerous', 'ACTIVE');
INSERT INTO missions (missionname, location, startTime, endTime, maxEndTime, description, status) VALUES ('mission000','Slovakia', '2015-04-01', '2015-05-30', '2016-04-01', 'top secret', 'ongoing');
INSERT INTO assignments (agentId, missionId, startDate, endDate) VALUES (1, 1, '2015-04-02', '2015-05-30');