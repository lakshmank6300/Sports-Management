
const urlParams = new URLSearchParams(window.location.search);
const collegename = urlParams.get("college");
const desig = urlParams.get("designation");
const nam = urlParams.get("facultyname");
const purpose = urlParams.get("purpose");
const telephone = urlParams.get("telephone");
const NoOfStudents = urlParams.get("nostudents");
const DateOfDuty = urlParams.get("DateofDuty");
const DateOfRelieving = urlParams.get("DateofRelieving");
const BankAccNo = urlParams.get("bankacc");
const ifsc = urlParams.get("ifsc");
const day = urlParams.get("nodays");
var designationList={"asc":"Associate Proffeser","pro":"Professor","assist":"Assistant Professor"};
var purposeList={"paper":"Paper Valuation","viva":"Project Viva","lab":"Lab External"};
var collegeList={"prag":"Pragathi Eng. College","bvc":"BVC Eng. College","vishnu":"Vishnu Eng. college","srkr":"SRKR Eng.college","kl":"KL University"}
var collnames={"prag":100,"bvc":100,"vishnu":100,"srkr":100,"kl":150};
var purp={"paper": 20,"viva":100,"lab":20};
var des={"asc":150,"pro":200,"assist":100};
const start = new Date(DateOfDuty);
const end = new Date(DateOfRelieving);
const st =start.toLocaleDateString();
const en = end.toLocaleDateString();
const differenceInMilliseconds = Math.abs(end - start);
const millisecondsInADay = 1000 * 60 * 60 * 24;
const days = Math.round(differenceInMilliseconds / millisecondsInADay);
const cost= des[desig]+collnames[collegename]+purp[purpose]*NoOfStudents;
const myArray=[nam,designationList[desig],collegeList[collegename],purposeList[purpose],telephone,st,en,NoOfStudents,days,BankAccNo,ifsc];
for(let i=0;i<11;i++)
{
    document.getElementById("p"+(i+1)).innerHTML=""+myArray[i];
}
document.getElementById("printCost").innerHTML=cost;


// async function appendDataToCSV(existingCSVFilePath, myArray) {
//   // Read the existing CSV file
//   const existingCSVResponse = await fetch(existingCSVFilePath);
//   const existingCSVData = await Papa.parse(await existingCSVResponse.text());

//   // Combine existing and new data
//   const combinedData = [...existingCSVData.data, myArray];

//   // Convert combined data to CSV string
//   const updatedCSVString = Papa.unparse(combinedData);

//   // Save the updated CSV file
//   const blob = new Blob([updatedCSVString], { type: 'text/csv' });
//   saveAs(blob, 'data.csv');
// }

// // Example usage
// const existingCSVFilePath = 'data.csv';


// appendDataToCSV(existingCSVFilePath, myArray);