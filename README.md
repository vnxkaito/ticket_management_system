# Ticket Management System

### API Endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | /auth/users/register | Public | Register a new user |
| POST | /auth/users/login | Public | Login and receive JWT |
| GET | /auth/users/verify/{token} | Public | Verify email address |
| POST | /auth/users/forgot-password | Public | Request password reset email |
| POST | /auth/users/reset-password | Public | Reset password using token |
| PUT | /auth/users/change-password | Authenticated | Change current password |
| PUT | /auth/users/profile | Authenticated | Update profile |
| POST | /auth/users/profile/picture | Authenticated | Upload profile picture |
| GET | /auth/users/profile/picture | Authenticated | Get profile picture |
| GET | /api/admin/users | ADMIN | Get all users |
| PUT | /api/admin/users/{userId}/activate | ADMIN | Activate a user |
| PUT | /api/admin/users/{userId}/deactivate | ADMIN | Deactivate a user |
| PUT | /api/admin/users/{userId}/roles/{roleId} | ADMIN | Assign role to user |
| DELETE | /api/admin/users/{userId}/roles/{roleId} | ADMIN | Remove role from user |
| DELETE | /api/admin/users/{userId} | ADMIN | Delete user (soft delete if ADMIN) |
| POST | /api/roles | ADMIN | Create a role |
| GET | /api/roles | ADMIN | Get all roles |
| GET | /api/roles/{id} | ADMIN | Get role by ID |
| PUT | /api/roles/{id} | ADMIN | Update a role |
| DELETE | /api/roles/{id} | ADMIN | Delete a role |
| POST | /api/teams | ADMIN | Create a team |
| GET | /api/teams | ADMIN, AGENT | Get all teams |
| GET | /api/teams/{id} | ADMIN, AGENT | Get team by ID |
| PUT | /api/teams/{id} | ADMIN | Update a team |
| DELETE | /api/teams/{id} | ADMIN | Delete a team |
| PUT | /api/teams/{teamId}/members/{userId} | ADMIN | Add user to team |
| DELETE | /api/teams/{teamId}/members/{userId} | ADMIN | Remove user from team |
| GET | /api/teams/{teamId}/members | ADMIN, AGENT | Get team members |
| POST | /api/categories | ADMIN | Create a category |
| GET | /api/categories | Public | Get all categories |
| GET | /api/categories/{id} | Public | Get category by ID |
| PUT | /api/categories/{id} | ADMIN | Update a category |
| DELETE | /api/categories/{id} | ADMIN | Delete a category |
| POST | /api/tickets | Authenticated | Create a ticket |
| GET | /api/tickets | ADMIN, AGENT | Get all tickets |
| GET | /api/tickets/{id} | Authenticated | Get ticket by ID |
| GET | /api/tickets/my | Authenticated | Get current user's tickets |
| GET | /api/tickets/assigned | ADMIN, AGENT | Get tickets assigned to current agent |
| GET | /api/tickets/team/{teamId} | ADMIN, AGENT | Get tickets by team |
| GET | /api/tickets/filter | ADMIN, AGENT | Filter tickets |
| PUT | /api/tickets/{ticketId}/claim | ADMIN, AGENT | Claim a ticket |
| PUT | /api/tickets/{ticketId}/assign/agent/{agentId} | ADMIN, AGENT | Assign ticket to agent |
| PUT | /api/tickets/{ticketId}/assign/team/{teamId} | ADMIN, AGENT | Assign ticket to team |
| PUT | /api/tickets/{ticketId}/reassign | ADMIN, AGENT | Reassign ticket |
| PUT | /api/tickets/{ticketId}/status | ADMIN, AGENT | Change ticket status |
| PUT | /api/tickets/{ticketId}/escalate | ADMIN, AGENT | Escalate a ticket |
| DELETE | /api/tickets/{ticketId} | ADMIN | Delete a ticket |
| POST | /api/tickets/{ticketId}/comments | Authenticated | Add a comment |
| GET | /api/tickets/{ticketId}/comments | ADMIN, AGENT | Get all comments |
| GET | /api/tickets/{ticketId}/comments/public | Authenticated | Get public comments |
| GET | /api/tickets/{ticketId}/comments/internal | ADMIN, AGENT | Get internal comments |
| GET | /api/tickets/{ticketId}/comments/{commentId} | Authenticated | Get comment by ID |
| PUT | /api/tickets/{ticketId}/comments/{commentId} | Authenticated | Update a comment |
| DELETE | /api/tickets/{ticketId}/comments/{commentId} | ADMIN, AGENT | Delete a comment |

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