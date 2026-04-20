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
- As a user, I want to recover my account by resetting my password so that I can regain access if I forget it.
- As a user, I want to change my password so that I can keep my account secure.
- As a user, I want to update my profile including my profile picture so that my information stays current.
#### Admin
- As an admin, I want to activate or deactivate user accounts so that I can control system access.
- As an admin, I want to assign roles to users so that permissions are managed correctly.
- As an admin, I want to remove roles from users so that permissions can be revoked.

## User, Role, and Team Management
#### Admin
- As an admin, I want to create roles so that users can be assigned proper permissions.
- As an admin, I want to view, update, and delete roles so that role definitions stay accurate.
- As an admin, I want to create teams so that agents can be grouped operationally.
- As an admin, I want to update and delete teams so that team structure stays current.
- As an admin, I want to add users to teams so that workload assignment can be managed.
- As an admin, I want to remove users from teams so that team membership stays accurate.
- As an admin, I want to view all users so that I can manage access and structure.
#### Agent
- As an agent, I want to view teams and their members so that I can understand team structure.

## Ticket Categories & Configuration
#### Admin
- As an admin, I want to create ticket categories so that tickets can be classified by type.
- As an admin, I want to define default priority per category so that tickets start with the correct urgency.
- As an admin, I want to update and delete ticket categories so that the category list stays relevant.

## Ticket Creation & Viewing
#### Customer
- As a customer, I want to create a ticket with subject, description, and category so that I can request support.
- As a customer, I want to view my submitted tickets so that I can track progress.
- As a customer, I want to view ticket details so that I can see status and updates.
- As a customer, I want to add comments to my tickets so that I can provide more information.
#### Agent
- As an agent, I want to view tickets assigned to me so that I can work on them.
- As an agent, I want to view tickets assigned to my team so that I can pick up work.
- As an agent, I want to filter tickets by status, priority, category, and team so that I can manage workload efficiently.
#### Admin
- As an admin, I want to view all tickets so that I can oversee the full system.

## Ticket Assignment
#### Agent
- As an agent, I want to claim an available ticket so that I can start working on it.
- As an agent, I want only one agent to be able to claim a ticket so that duplicate assignment does not occur.
- As an agent, I want to assign a ticket to a specific agent so that work can be distributed intentionally.
- As an agent, I want to assign a ticket to a team so that the right group handles it.
- As an agent, I want to reassign a ticket to another agent or team so that blocked or overloaded work can be handled.
- As an agent, I want to escalate a ticket so that urgent issues get additional attention.


## Ticket Workflow
#### Agent
- As an agent, I want to change ticket status so that work progress is reflected.
- As an agent, I want to resolve a ticket so that completed work is recorded.
- As an agent, I want to add internal comments so that internal collaboration can happen without exposing details to the customer.
- As an agent, I want to add public comments so that the customer can be informed.
- As an agent, I want to view all comments on a ticket including internal ones so that I have full context.
#### Customer
- As a customer, I want to see public comments on my ticket so that I understand updates.
- As a customer, I do not want to see internal comments so that internal operational notes remain private.

## Security & Access Control
#### Customer
- As a customer, I want to access only my own tickets so that my data remains private.
#### Agent
- As an agent, I want access to tickets and team data relevant to my work so that I can operate effectively.
#### Admin
- As an admin, I want full access to system management features so that I can administer the platform.

## Concurrency
- As the system, I want to prevent a ticket from being escalated after it has been resolved so that conflicting concurrent updates are rejected.
- As the system, I want to time out lock acquisition so that deadlocks between concurrent ticket operations are broken automatically.

## Trello
[Trello Link](https://trello.com/b/NyHSRsYE/jdb-project3)


---

# ERD
![ERD](images/ERD.png)