jsonObject = {};
Array.from(document.querySelectorAll(".table-responsive tbody tr")).forEach(e =>{
        tds =  e.querySelectorAll("td");
        jsonObject[tds[0].innerHTML] = tds[1].innerHTML;
    }
);
return JSON.stringify(jsonObject);