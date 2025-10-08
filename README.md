# CMPUT 301 : Lab 5 - Firestore

## Student Details

- **Full Name:** `<Junayed Ahmed>`
- **CCID:** `<junayed>`

## References and Resources

- CMPUT 301 Lab 5 Firestore Integration Demo and TA Guide
- Firebase Firestore Android Documentation
- Android Developers Docs – ListView and AlertDialog


## Verbal Collaboration
- N/A (Didn't Colleborate')

## Design and Implementation

### Objective

Develop an Android app that allows users to add, edit, and delete cities stored in Firebase Firestore, ensuring persistence across app restarts.

### User Guide
- Launch the app — cities from Firestore automatically load.
- Tap Add City — enter a name and province, then confirm.
- Single tap a city — selects it for deletion.
- Tap Delete City — confirm deletion; the city is removed.
- Double-tap a city — edit its details; the update persists.
- Restart the app — all changes remain in sync with Firestore.

### Testing Checklist
 - Add City — city appears in Firestore and ListView.
 - Delete City — city removed from Firestore and list.
 - Edit City — updated in Firestore and UI.
 - Restart App — data persists.
 - Toast and dialog confirmations appear correctly.
