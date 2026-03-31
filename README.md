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
##### <<< Anything above this line should be finished before ( Friday | 3-4-2026 ) >>>
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
#### Admin
- As an admin, I want to create ticket categories so that tickets can be classified by type.
- As an admin, I want to define default priority per category so that tickets start with the correct urgency.
- As an admin, I want to define first response SLA per category so that response expectations are standardized.
- As an admin, I want to define resolution SLA per category so that closure expecations are standardized.
- As an admin, I want to define routing strategy per category so that tickets are automatically directed correctly.

## Ticket Creation & Viewing
#### Customer
- As a customer, I want to create a ticket with subject, description, and category so that i can request support.
- As a customer, I want to view my submitted tickets so that I can track progress.
- As a customer, I want to view ticket details so that I can see status, comments, and updates.
- As a customer, I want to add comments to my tickets so that I can provide more information.
- As a customer, I want to receive udpates when my ticket changes so that I stay informed.
#### Agent
- As an agent, I want to view tickets assigned to me so that I can work on them.
- As an agent, I want to view tickets available to my team so that i can pick up work.
- As an agent, I want to filter tickets by status, priority, and category so that I can manage workload efficiently.
#### Supervisor
- As a supervisor, I want to view all tickets for my team so that I can monitor workload and performance.
#### Admin
- As an admin, I want to view all tickets so that I can oversee the full system.

##### <<< Anything above this line should be finished before ( Saturday | 4-4-2026 ) >>>

## Ticket Assignment & Routing
#### System
- As the system, I want to auto-route a new ticket based on category so that it reaches the correct team or queue.
- As the system, I want to calculate SLA due dates when a ticket is created so that time-based monitoring can begin immediately.
#### Agents
- As an agent, I want to claim an available ticket so that I can start working on it.
- As an agent, I want only one agent to be able to claim a ticket so that duplicate assignment does not occur.
#### Supervisor
- As a supervisor, I want to manually assign a ticket to an agent so that work can be distributed intentionally.
- As a supervisor, I want to reassign a ticket to another agent or team so that blocked or overloaded work can be handled.
- As a supervisor, I want to escalate a ticket manually so that urgent issues get additional attention.

## Ticket Workflow
#### Agent
- As an agent, I want to change ticket status from open to in progress so taht work progress is reflected.
- As an agent, I want to mark a tikcet as waiting on customer so that delays caused by missing customer input are visible.
- As an agent, I want to resolve a ticket so that completed work is recorded.
- As an agent, I want to add internal comments so that internal collaboration can happen without exposing details to the customer.
- As an agent, I want to add public comments so that the customer can be informed.
#### Customer
- As a customer, I want to see public comments on my ticket so that I understand updates.
- As a customer, I do not want to see internal comments so that internal operational notes remain private.
#### Supervisor
- As a supervisor, I want to close resolve tickets so that completed cases can be finalized.

## SLA Monitoring
###

## Security & Access Control
#### Customer
- As a customer, I want to acess only my own tickets so that my data remains private.
#### Supervisor
- As a supervisor, I want broader access to team tickets so that I can manage operations.
#### Admin
- As an admin, I want full access to system management features so that I can administer the platform.



## Notifications

## Audit Trail

##### <<< Anything above this line should be finished before ( Sunday | 5-4-2026 ) >>>

## Realtime updates

## Reporting & Dashboards


## Load & Reliability

##### <<< Anything above this line should be finished before ( Wednesday | 8-4-2026 ) >>>

#### if progress matched the plan
- Create frontend (Thursday, Friday, and Saturday)
- Run the project on an AWS EC2 instance ( Saturday )
- Enhance the API endpoints ( Saturday and Sunday )
- Prepare presentation



### concurrency scenarios
- A user escalate a ticket at the same time an agent has resolved it.
- Two users are locking different tickets then each user is trying to move to the ticket locked by the other user causing a deadlock.
- A background job checks a field in the ticket data to take action (like sending a notification) but between checking the field and doing the action, the data is changed, resulting a faulty action.

## Trello
[Trello Link](https://trello.com/b/NyHSRsYE/jdb-project3)


---

# ERD
![ERD](images/ERD.png)