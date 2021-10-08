

codeGenerator();




let select_id = 0;
$('#sale').submit((event) => {
    event.preventDefault();











    const fatura_No = $('#fatura_No').val()
    const c_name = $("#cuname").val()
    const p_name = $("#pname").val()
    const p_price = $("#productPrice").val()
    const p_amount = $("#productAmount").val()
    const pro_type = $("#protype").val()
    const t_ax_type = $("#tax_type").val()
    const p_discount = $("#pdiscount").val()
    const pur_type = $("#purType").val()
    const notE = $("#note").val()





    const obj = {
        cname: c_name,
        pname: p_name,
        faturaNo: fatura_No,
        pprice: p_price,
        pamount: p_amount,
        proType: pro_type,
        tax_type: t_ax_type,
        pdiscount: p_discount,
        purType: pur_type,
        note: notE,
    }
    console.log(obj)


    allSales();


     if (select_id != 0) {
         //Sale için tanımlanan sale id ////////////////
         obj["sid"] = select_id;
     }
     $.ajax({

         url: './sale/nsert',
        type: 'POST',
        data: JSON.stringify(obj),
        dataType: 'json',
       contentType : 'application/json; charset=utf-8',

         success: function (data) {


             console.log(obj)
             if (data > 0) {

                 alert("İşlem Başarılı")
                 allSales();

             } else {
                // alert("İşlem sırasında hata oluştu!");

                 allSales();
             }
         },
         error: function (err) {
             console.log(err)
             alert("İşlem işlemi sırasında bir hata oluştu!");
         }
     })

     fncReset();
 })



    function allSales() {

        $.ajax({
            url: './sale/list',
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


    function fncReset() {
        select_id = 0;
        $('#sale').trigger("reset");
        allSales();
    }

    let globalArr = []

    function createRow(data) {
        globalArr = data;
        let html = ``




        for (let i = 0; i < data.length; i++) {

            const item = data[i];

            let pt = '';
            let tt = '';

            if (item.purType == 1) {
                pt = 'Nakit'
            } else if (item.purType == 2) {
                pt = 'Kredi Kartı'
            } else if (item.purType == 3) {
                pt = 'Banka Havalesi'
            }


            if (item.tax_type == 0) {
                tt = 'Dahil'
            } else if (item.tax_type == 1) {
                tt = '%1'
            } else if (item.tax_type == 2) {
                tt = '%8'
            } else if (item.tax_type == 3) {
                tt = '%18'
            }


            html += `<tr role="row" class="odd">
           <td class="text-right" >
             <div class="btn-group" role="group" aria-label="Basic mixed styles example">
              <button onclick="fncOrderDelete(`+item.sid+`)" type="button" class="btn btn-outline-primary "><i class="far fa-trash-alt"></i></button>
              <button onclick="fncProductUpdate(`+i+`)" data-bs-toggle="modal" data-bs-target="#kayitliMusteriSatis" type="button" class="btn btn-outline-primary "><i class="fas fa-pencil-alt"></i></button>
             </div>
           </td>
              
              <td>` + item.sid + `</td>
              <td>` + dateToFormat(item.saleDate)+ `</td>       
              <td>` + item.cname + `</td>
              <td>` + pt + `</td>
              <td>` + item.grossPrice + `</td>
              <td>` + item.netPrice + `</td>
              <td>` + tt + `</td>
              <td>` + item.pdiscount + `</td>
              <td>` + item.totalPrice + `</td>
              <td>` + item.note + `</td>
              
            </tr>`;

        }
        $('#tableRow').html(html);

    }


allSales();

     function codeGenerator(){
         const date = new Date();
         const time = date.getTime();
         const key = time.toString().substring(4);
         $('#fatura_No').val(key)
 }


    function fncOrderDelete(sid) {
        let answer = confirm("Silmek istediğinizden emin misniz?");
        if (answer) {

            $.ajax({
                url: './sale/sDelete/' + sid,
                type: 'DELETE',
                dataType: 'text',
                success: function (data) {
                    console.log(typeof data)
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

    function fncOrderDetail(i) {
        const item = globalArr[i];

        $("#cuname").val(item.cname == "" ? '----' : item.cname);
        $("#pname").val(item.pname == "" ? '----' : item.pname);
        $("#productPrice").val(item.pprice == "" ? '----' : item.pprice);
        $("#productAmount").val(item.pamount == "" ? '----' : item.pamount);

        $("#protype").val(item.proType == "" ? '----' : item.proType);

        $("#tax_type").val(item.tax_type == "" ? '----' : item.tax_type);

        $("#pdiscount").val(item.pdiscount == "" ? '----' : item.pdiscount);

        $("#purType").val(item.purType == "" ? '----' : item.purType);
        $("#note").val(item.note == "" ? '----' : item.note);
    }
function dateToFormat( stDate ) {
    const dt = new Date(stDate);

    return ('0' + dt.getDate()).slice(-2) + "-" + ('0' + (dt.getMonth() +1)).slice(-2)+ "-" + dt.getFullYear() + " " + dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
}

function fncProductUpdate( i ) {
    debugger;
    const item = globalArr[i];
    select_id = item.sid

    $("#cuname").val(item.cname)
    $("#pname").val(item.pname)
    $("#productPrice").val(item.pprice)
    $("#productAmount").val(item.pamount)

    $("#protype").val(item.proType)

    $("#tax_type").val(item.tax_type)

    $("#pdiscount").val(item.pdiscount);

    $("#purType").val(item.purType)
    $("#note").val(item.note)


}
$('#sale').trigger("reset");

function statusChanger(faturaNo){
    const item = globalArr[0];
    faturaNo = item.faturaNo
     console.log(item)
    $.ajax({
        url: './sale/sChange/'+faturaNo,
        type: 'GET',
        dataType: 'text',
        success: function (data){
            if(data>0){

                allSales();
              codeGenerator();
            }
            else{
            alert("Satın alma sırasında hata meydana geldi!")
            }
        },
        error: function (err){
            console.log(err)

        }

    })


}

