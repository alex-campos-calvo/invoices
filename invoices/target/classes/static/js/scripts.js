

/*
$(document).ready(function($) {
    $(".enter-invoice").click(function() {
        window.location = $(this).data("href");
    });
});

$('#invoice-edit-modal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var url = '/get-invoice/edit/' + button.data('id');
    $(this).find('#invoice-edit-modal-content').load(url, function(){
        $('.delete-line').click(function(e){
            $(this).parent().parent().remove();
        });

        $('#add-line-edit-invoice').click(function (e) {
            var line;
            $('[data-lines-edit]').each(function(i,elem){
                line = elem;
            });
            line = $(line).clone();
            $(line).attr('data-lines-edit', parseInt($(line).attr('data-lines-edit'))+1);
            $(line).attr('data-id', '');
            $(line).find("input[type=text]").val('');
            $(line).find(".description").text('');
            $(line).find("input[type=number]").val('');
            $(this).before($(line));
        });

        $('#edit-invoice').click(function(e){
            var item = {};
            item["id"] = $(this).attr('data-id');
            item["name"] = $('#edit-name').val();
            item["nif"] = $('#edit-nif').val();
            item["address"] = $('#edit-address').val();
            item["zip_code"] = $('#edit-zip-code').val();
            item["state"] = $('#edit-state').val();
            item["lines"] = [];
            $('[data-lines-edit]').each(function(i,elem){
                var line = {};
                line["id"] = $(this).attr('data-id');
                line["quantity"] = $(elem).find("input[type=text]").val();
                line["description"] = $(elem).find(".description").text();
                line["price"] = $(elem).find("input[type=number]").val();
                item["lines"].push(line);
            });

            $.ajax({
                type: "POST",
                url: "/update-invoice",
                contentType: "application/json",
                data: JSON.stringify(item),
                success: function(data){
                    location.reload();
                }
            });
        });
    });
});

$('#invoice-send-modal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var url = '/get-invoice/send/' + button.data('id');
    $(this).find('#invoice-send-modal-content').load(url);
});

$('#delete-modal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var id = button.data('id');
    var num = button.data('num')
    var modal = $(this)
    modal.find('.modal-body span').text('¿Estás seguro de que deseas eliminar la factura ' + num + ' ?');
    $('#delete-invoice').data('id', id);
});

$('#delete-invoice').click(function(e){
    var item = $(this).data('id');
    console.log(item);
    $.ajax({
        type: "PUT",
        url: "/delete-invoice/" + item,
        success: function(data){
            location.reload();
        }
    });
});

$('#create-invoice').click(function(e){
    var item = {};
    item["name"] = $('#new-invoice-name').val();
    item["nif"] = $('#new-invoice-nif').val();
    item["address"] = $('#new-invoice-address').val();
    item["zip_code"] = $('#new-invoice-zip-code').val();
    item["state"] = $('#new-invoice-state').val();
    item["lines"] = [];
    $('[data-lines-new-invoice]').each(function(i,elem){
        var line = {};
        line["quantity"] = $(elem).find("input[type=text]").val();
        line["description"] = $(elem).find(".description").text();
        line["price"] = $(elem).find("input[type=number]").val();
        item["lines"].push(line);
    });

    console.log(JSON.stringify(item));

    $.ajax({
        type: "POST",
        url: "/save-new-invoice",
        contentType: "application/json",
        data: JSON.stringify(item),
        success: function(data){
            location.reload();
        }
    });
});

$('#add-line-new-invoice').click(function(e){
    var line;
    $('[data-lines-new-invoice]').each(function(i,elem){
        line = elem;
    });
    line = $(line).clone();
    $(line).attr('data-lines-new-invoice', parseInt($(line).attr('data-lines-new-invoice'))+1);
    $(this).before($(line));

    $('.delete-line').click(function(e){
        $(this).parent().parent().remove();
    });
});

$('.delete-line').click(function(e){
    $(this).parent().parent().remove();
});*/
