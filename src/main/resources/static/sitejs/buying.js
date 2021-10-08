codeGenerator();
allBuying();
orderList();
let select_idx=0;
$('#buyingInsert').submit((event) => {
    event.preventDefault();


    const fatura_No = $('#faturaNo').val()
    const t_name = $('#tname').val()
    const p_name = $("#pname").val()
    const b_note = $("#bnote").val()
    const pro_price = $("#product_Price").val()
    const pro_amount = $("#product_Amount").val()
    const pro_birim = $("#proBirim").val()
    const tax_type = $("#taxType").val()
    const p_discount = $("#pdiscount").val()
    const pur_type = $("#purType").val()

    const object = {

        tname:t_name,
        pname: p_name,
        bfaturaNo: fatura_No,
        bnote: b_note,
        proprice: pro_price,
        proamount: pro_amount,
        probirim: pro_birim,
        taxtype: tax_type,
        pdiscount: p_discount,
        purtype: pur_type,

    }

    console.log(object)


    if (select_idx != 0) {
        //Sale için tanımlanan sale id ////////////////
        object["bid"] = select_idx;
    }
    $.ajax({

        url: './buying/binsert',
        type: 'POST',
        data: JSON.stringify(object),
        dataType: 'json',
        contentType : 'application/json; charset=utf-8',
        success: function (data) {

            console.log(object)
            if (data > 0) {

                alert("İşlem Başarılı")

                allBuying();

            } else {
                // alert("İşlem sırasında hata oluştu!");

                allBuying();
            }
        },
        error: function (err) {
            console.log(err)
            alert("İşlem işlemi sırasında bir hata oluştu!");
        }
    })
    allBuying();
    fncReset();


}  )


function allBuying(){

    $.ajax({
        url: './buying/blist',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            console.log(data)
            createRow(data);
        },
        error: function (err) {
            console.log(err)
        }
    })

}




let globalArr = []

function createRow(data) {
    globalArr = data;
    let html = ``

    for (let i = 0; i < data.length; i++) {

        const item = data[i];

        let bpt = '';
        let btt = '';
        let bpb = '';
        if (item.purtype == 1) {
            bpt = 'Nakit'
        } else if (item.purtype == 2) {
            bpt = 'Kredi Kartı'
        } else if (item.purtype == 3) {
           bpt = 'Banka Havalesi'
        }


        if (item.taxtype == 0) {
            btt = 'Dahil'
        } else if (item.taxtype == 1) {
            btt = '%1'
        } else if (item.taxtype == 2) {
            btt = '%8'
        } else if (item.taxtype == 3) {
            btt = '%18'
        }

        if(item.probirim ==1){
            bpb = 'Mililitre'
        }
        else if(item.probirim ==2){
            bpb = 'g'
        }
        else if(item.probirim ==3){
            bpb = 'Kutu'
        }
        else if(item.probirim ==4){
            bpb = 'Adet'
        }
        html += `<tr role="row" class="odd">

           <td class="text-right" >
             <div class="btn-group" role="group" aria-label="Basic mixed styles example">
              <button onclick="fncBProductDelete(`+item.bid+`)" type="button" class="btn btn-outline-primary "><i class="far fa-trash-alt"></i></button>
              <button onclick="fncBProductUpdate(`+i+`)" data-bs-toggle="modal" data-bs-target="#kayitliTedarikcidenAlis" type="button" class="btn btn-outline-primary "><i class="fas fa-pencil-alt"></i></button> 
             </div>
           </td>
             
              <td>`+item.bid+`</td>
              <td>`+dateFormat(item.buyingDate)+`</td>       
              <td>`+item.pname+`</td>
              <td>`+item.tname+`</td>           
              <td>`+bpt+`</td>
              <td>`+item.proprice+`</td>
              <td>`+item.proamount+`</td>
              <td>`+item.probirim+`</td>            
              <td>`+btt+`</td>
              <td>`+item.pdiscount+`</td>
              <td>`+item.btotalPrice+`</td>
              <td>`+item.bnote+`</td>
              
            </tr>`;

    }
    $('#tableRow').html(html);

}
allBuying();

function codeGenerator(){
    const date = new Date();
    const time = date.getTime();
    const key = time.toString().substring(4);
    $('#faturaNo').val(key)
}

function modalReset() {
    $('#kayitliTedarikcidenAlis').trigger("reset");
}

function fncReset() {
    select_idx = 0;
    $('#buyingInsert').trigger("reset");
    allBuying();
}

function fncTReset() {
    select_idx = 0;
    $('#buying').trigger("reset");
    allBuying();
}


function dateFormat( bDate ) {
    const dt = new Date(bDate);

    return ('0' + dt.getDate()).slice(-2) + "-" + ('0' + (dt.getMonth() +1)).slice(-2)+ "-" + dt.getFullYear() + " " + dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
}

function bstatusChanger(bfaturaNo){
    const item = globalArr[0];
    bfaturaNo = item.bfaturaNo
    console.log(item)
    $.ajax({
        url: './buying/sChanger/'+bfaturaNo,
        type: 'GET',
        dataType: 'text',
        success: function (data){
            if(data>0){

                allBuying();
                orderList();
                codeGenerator();
            }
            else{
                alert("Alış sırasında hata meydana geldi!")
            }
        },
        error: function (err){
            console.log(err)

        }



    })


}
   //////////////////////Burası Sorgu yazılacak mappinge gidecek




let globalSumArr = []
function createRowSum(data){
    globalSumArr = data;
    console.log(data)

    let html = ``

    for (let i = 0; i < data.length; i++) {

        const item = data[i];

        let bpt = '';
        let btt = '';

        if (item.purtype == 1) {
            bpt = 'Nakit'
        } else if (item.purtype == 2) {
            bpt = 'Kredi Kartı'
        } else if (item.purtype == 3) {
            bpt = 'Banka Havalesi'
        }


        if (item.taxtype == 0) {
            btt = 'Dahil'
        } else if (item.taxtype == 1) {
            btt = '%1'
        } else if (item.taxtype == 2) {
            btt = '%8'
        } else if (item.taxtype == 3) {
            btt = '%18'
        }


        html += `<tr role="row" class="odd">
           <td class="text-right" >
             <div class="btn-group" role="group" aria-label="Basic mixed styles example">
              <button onclick="fncOrderDelete(`+item.bid+`)" type="button" class="btn btn-outline-primary "><i class="far fa-trash-alt"></i></button>
              <button onclick="fncDetailUpdate(`+item.bfaturaNo+`)" data-bs-toggle="modal" data-bs-target="#buyingdetail" type="button" class="btn btn-outline-primary "><i class="fas fa-pencil-alt"></i></button>
             </div>
           </td>
             
      
              <td>`+dateFormat(item.buyingDate)+`</td>       
              <td>`+item.bfaturaNo+`</td>
              <td>`+item.tname+`</td>                        
              <td>`+bpt+`</td>             
              <td>`+item.bgrossPrice+`</td>  
            
              <td>`+item.bnetPrice+`</td>  
              <td>`+btt+`</td>
              <td>`+item.pdiscount+`</td>
              <td>`+item.btotalPrice+`</td>            
            </tr>`;

    }
    $('#tableRowSum').html(html);

}



function fncOrderDelete(bid) {
    let answer = confirm("Silmek istediğinizden emin misniz?");
    if (answer) {

        $.ajax({
            url: './buying/bDelete/' + bid,
            type: 'DELETE',
            dataType: 'text',
            success: function (data) {
                console.log(typeof data)
                if (data != "0") {
                    orderList();
                    allSales();
                    fncReset();
                } else {
                    alert("Silme sırasında bir hata oluştu!");
                }
            },
            error: function (err) {
                console.log(err)
            }
        })
    }

}

function orderList(){
    $.ajax({
        url:'./buying/cList/',
        type: 'GET',
        dataType: 'JSON',
        contentType: 'application/json; charset=utf-8',
        success: function (data){
            console.log(data)
            createRowSum(data);
        },
        error: function (err){
            console.log(err)
        }


    })

}

function fncBProductDelete(bid){
    let answer = confirm("Silmek istediğinizden emin misniz?");
    if (answer) {

        $.ajax({
            url: './buying/pDelete/' + bid,
            type: 'DELETE',
            dataType: 'text',
            success: function (data) {
                if (data != "0") {
                    fncReset();
                } else {
                    alert("Silme sırasında bir hata oluştu!");
                }
            },
            error: function (err) {
                console.log(err)
            }
        })
    }
}

allBuying();

function fncBProductUpdate(i){
 debugger;
 const item = globalArr[i];
 select_idx = item.bid
    $("#tname").val(item.tname)
    $("#pname").val(item.pname)

    $("#product_Price").val(item.proprice)
    $("#product_Amount").val(item.proamount)
    $("#proBirim").val(item.probirim)
    $("#protype").val(item.proType)

    $("#taxType").val(item.taxtype)

    $("#pdiscount").val(item.pdiscount);

    $("#purType").val(item.purtype)
    $("#bnote").val(item.bnote)


}

function fncDetailUpdate(bfaturaNo){


   $.ajax({
       url: './buying/detail/' + bfaturaNo,
       type: 'GET',

       success: function (ddata) {
           console.log(ddata)
           createDetailRow(ddata);

       },
       error: function (err) {
           console.log(err)
       }

   })
}


let globalDetailArr = []

function createDetailRow(ddata) {
    globalDetailArr = ddata;
    let html = ``

    for (let i = 0; i < ddata.length; i++) {

        const item = ddata[i];

        let dbpt = '';
        let dbtt = '';
        let dbpb = '';
        if (item.purtype == 1) {
            dbpt = 'Nakit'
        } else if (item.purtype == 2) {
            dbpt = 'Kredi Kartı'
        } else if (item.purtype == 3) {
            dbpt = 'Banka Havalesi'
        }


        if (item.taxtype == 0) {
            dbtt = 'Dahil'
        } else if (item.taxtype == 1) {
            dbtt = '%1'
        } else if (item.taxtype == 2) {
            dbtt = '%8'
        } else if (item.taxtype == 3) {
            dbtt = '%18'
        }

        if(item.probirim ==1){
            dbpb = 'Mililitre'
        }
        else if(item.probirim ==2){
            dbpb = 'g'
        }
        else if(item.probirim ==3){
            dbpb = 'Kutu'
        }
        else if(item.probirim ==4){
            dbpb = 'Adet'
        }
        html += `<tr role="row" class="odd">

             
              <td>`+item.bid+`</td>
              <td>`+dateFormat(item.buyingDate)+`</td>       
              <td>`+item.pname+`</td>
              <td>`+item.tname+`</td>           
              <td>`+dbpt+`</td>
              <td>`+item.proprice+`</td>
              <td>`+item.proamount+`</td>
              <td>`+dbpb+`</td>            
              <td>`+dbtt+`</td>
              <td>`+item.pdiscount+`</td>
              <td>`+item.btotalPrice+`</td>
              <td>`+item.bnote+`</td>
              
            </tr>`;

    }
    $('#detailRow').html(html);

}

$('#buying').submit((tevent) => {

    tevent.preventDefault();

    const s_name = $('#sname').val()
    const s_email = $('#semail').val()
    const  telefon = $("#telefon").val()

    const sobj = {

        tname:s_name,
        temail: s_email,
        telefon: telefon,
    }



    $.ajax({

        url: './buying/supp',
        type: 'POST',
        data: JSON.stringify(sobj),
        dataType: 'json',
        contentType : 'application/json; charset=utf-8',
        success: function (data) {

            console.log(sobj)
            if (data > 0) {

                alert("İşlem Başarılı")

            } else {
                // alert("İşlem sırasında hata oluştu!");

            }
        },
        error: function (err) {
            console.log(err)
           // alert("İşlem işlemi sırasında bir hata oluştu!");
        }

    })
    fncTReset();
})
