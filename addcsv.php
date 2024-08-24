<?php
// Form handler script

// Get the form data
$facultyname = $_POST['facultyname'];
$designation = $_POST['designation'];
$college = $_POST['college'];
$purpose = $_POST['purpose'];
$telephone = $_POST['telephone'];
$DateofDuty = $_POST['DateofDuty'];
$DateofRelieving = $_POST['DateofRelieving'];
$nostudents = $_POST['nostudents'];
$bankacc = $_POST['bankacc'];
$ifsc = $_POST['ifsc'];
echo $ifsc;
// Open the CSV file in append mode
$fp = fopen('data.csv', 'a');

// Write the header row if the file is empty
if (filesize('data.csv') == 0) {
    fputcsv($fp, array('Faculty Name', 'Designation', 'College', 'Purpose', 'Telephone', 'Date of Duty', 'Date of Relieving', 'Number of Students', 'Bank Account', 'IFSC Code'));
}

// Write the form data to the CSV file
fputcsv($fp, array($facultyname, $designation, $college, $purpose, $telephone, $DateofDuty, $DateofRelieving, $nostudents, $bankacc, $ifsc));

// Close the file
fclose($fp);

// Redirect the user to a thank-you page or display a success message
header('Location: printPage.html');
exit;
?>
