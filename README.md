# MyEventBrite
My very first mobile apps with not much focus on the UI.😅😅

A Mobile Apps using Eventbrite API and Firebase API for monitoring a private event where each valid person can only have one ticket.

I found that although eventbrite allow us to organize a private event (can register through invitation link via email only), but other non-invited people can still use the same link to register a ticket too. So, I built a mobile apps to identify them by matching their registered emails and the emails I used to send (valid email) and finding out the duplicated valid emails.

All in all, my main goal is to allow a person with a valid email can register only once and no more.

<img alt="Attending" src="/screenshots/Attending.jpeg" width="30%" height="30%">    <img alt="Duplicate Email" src="/screenshots/Duplicate Email.jpeg" width="30%" height="30%">    <img alt="Valid Email" src="/screenshots/Valid Email.jpeg" width="30%" height="30%">   
## 1st Image: Attending
All the attendees data are extracted via **Eventbrite API** and shown on this screen with the searching function, counting number of attendees and sorting by country and seating. 

## 2nd Image: Duplicate Email
This is where the duplicated emails shown in this page after matching all the attendees data.

## 3rd Image: Valid Email
This page shows a list of invited attendees with their respective email addresses stored in **Firestore**. These email addresses are used to match with the attendees email address from Eventbrite. If a email address from Eventbrite cannot be found in the invited attendees'email addresses, his/her email address will be shown in the invalid email page. 
