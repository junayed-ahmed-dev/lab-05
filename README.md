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

## Architecture Overview
### Component Description
1. MainActivity.java Manages Firestore connection, handles add/edit/delete logic, and updates the UI through real-time listeners.
2. City.java Model class containing the name and province fields for each city.
3. CityArrayAdapter.java Custom adapter to display the list of cities in a ListView.
4. CityDialogFragment.java Dialog fragment used to add or edit city information.
5. activity_main.xml Layout containing the “Add City” and “Delete City” buttons and the ListView.

### Implementation Details
1. Firestore Connection: Uses FirebaseFirestore.getInstance() and the cities collection. Real-time updates handled via addSnapshotListener().
2. Add City: Tap Add City → opens dialog (CityDialogFragment). Enter name and province → new document saved to Firestore.
3. Select and Delete City: Single tap on a city → marks it as selected for deletion. Press Delete City → confirmation dialog appears. On confirmation, the city document is deleted from Firestore and removed from the local list. Displays a Toast message: “City deleted successfully.”
4. Edit City Double-tap on a city → opens dialog for editing. After saving, the Firestore document is overwritten with updated data.
5. Persistence Firestore’s snapshot listener ensures that changes (additions, deletions, edits) remain synchronized and persist through restarts.
6. User Feedback: Dialogs and Toasts provide confirmation for user actions.

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
