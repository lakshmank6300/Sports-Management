const DateOfDuty = document.getElementById("duty").value;
const DateOfRelieving = document.getElementById("relieving").value;

let myArray=[];
    function csv(){
        alert("Details Submitted Succesfully!!!");
        const collegename = document.getElementById("college").value;
        const desig = document.getElementById("designation").value;
        const name = document.getElementById("faculty").value;
        const purpose = document.getElementById("purpose").value;
        const telephone = document.getElementById("phone").value;
        const NoOfStudents = document.getElementById("nostudents").value;
        const DateOfDuty = document.getElementById("duty").value;
        const DateOfRelieving = document.getElementById("relieving").value;
        const BankAccNo = document.getElementById("bankacc").value;
        const ifsc = document.getElementById("ifsc").value;
        const day = document.getElementById("nodays").value;
        var rem =0
        var designationList={"asc":"Associate Proffeser","pro":"Professor","assist":"Assistant Professor"};
        var purposeList={"paper":"Paper Valuation","viva":"Project Viva","lab":"Lab External"};
        var collegeList={"prag":"Pragathi Eng. College","bvc":"BVC Eng. College","vishnu":"Vishnu Eng. college","srkr":"SRKR Eng.college","kl":"KL University"}
        var collnames={"prag":100,"bvc":100,"vishnu":100,"srkr":100,"kl":150};
        var purp={"paper": 20,"viva":100,"lab":20};
        var des={"asc":150,"pro":200,"assist":100};
        const start = new Date(DateOfDuty);
        const end = new Date(DateOfRelieving);
        const differenceInMilliseconds = Math.abs(end - start);
        const millisecondsInADay = 1000 * 60 * 60 * 24;
        const days = Math.round(differenceInMilliseconds / millisecondsInADay);
        const cost= des[desig]+collnames[collegename]+purp[purpose]*NoOfStudents;
        myArray=[name,designationList[desig],collegeList[collegename],purposeList[purpose],telephone,start,end,NoOfStudents,days,BankAccNo,ifsc];
}
function getdetails()
{
    const collegename = document.getElementById("college").value;
    const  desig = document.getElementById("designation").value;
    const name = document.getElementById("faculty").value;
    const purpose = document.getElementById("purpose").value;
    const telephone = document.getElementById("phone").value;
    const NoOfStudents = document.getElementById("nostudents").value;
    const DateOfDuty = document.getElementById("duty").value;
    const DateOfRelieving = document.getElementById("relieving").value;
    var rem =0
    var collnames={"prag":100,"bvc":100,"vishnu":100,"srkr":100,"kl":150};
    var purp={"paper": 20,"viva":100,"lab":20};
    const start = new Date(DateOfDuty);
    const end = new Date(DateOfRelieving);
    const differenceInMilliseconds = Math.abs(end - start);
    const millisecondsInADay = 1000 * 60 * 60 * 24;
    const days = Math.round(differenceInMilliseconds / millisecondsInADay);
    const cost= collnames[collegename]+purp[purpose]*NoOfStudents;
    const details= "No of days: "+days+"\nNo of Students: "+NoOfStudents+"\nTotalAmount: "+cost;
    window.alert(details)

    
}
