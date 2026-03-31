# Ticket Management System

### project overview
A ticket management system for customer support. An end user will raise a ticket which will be resolved by a support team.


## Authentication
#### Customer
- As a new user, I want to sign up with username, email, and password so that I can access the platform.
- As a new user, I want to receive an email verification link so that my account can be confirmed.
- As a new user, I want to verify my email so that I can log in.
- As a user, I want to log in with my credentials so that I can access my account.
- As a user, I want to be blocked from logging in before email verification so that only verified accounts are active.
- As a user, I want to request a new verification email so that I can verify my account if I missed or lost the first one.
- As a user, I want to log out so that my session is ended securely.
- As a user, I want to reset my password using a secure token so that I can recover my account.
#### Admin
- As an admin, I want to activate or deactivate user accounts so that I can control system access.
- As an admin, I want to assign roles to users so that permissions are managed correctly.

## User, Role, and Team Managment
#### Admin
- As an admin, I want to create roles so that users can be assigned proper permissions.
- As an admin, I want to create teams so that agents can be grouped operationally.
- As an admin, I want to add users to teams so that routing and workload assignment can be managed.
- As an admin, I want to remove users from teams so that team membership stays accurate.
- As an admin, I want to view all users with their roels and teams so that I can manage access and structure.
#### Supervisor
- As a supervisor, I want to view team members so that I can manage assignments within my team.

## Ticket Categories & Configuration

## Ticket Creation & Viewing
#### Customer
- As a customer, I want to create a ticket with subject, description, and category so that i can request support.

## Ticket Assignment & Routing

## Ticket Workflow

## SLA Monitoring

## Notifications

## Audit Trail

## Realtime updates

## Reporting & Dashboards

## Load & Reliability




### concurrency scenarios
- A user escalate a ticket at the same time an agent has resolved it.
- Two users are locking different tickets then each user is trying to move to the ticket locked by the other user causing a deadlock.
- A background job checks a field in the ticket data to take action (like sending a notification) but between checking the field and doing the action, the data is changed, resulting a faulty action.


![ERD](images/ERD.png)