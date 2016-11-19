
function addComment(commentId) {

    $.ajax({
        type: "GET",
        url: "localhost:3000/news/1",
        crossDomain: true,
        dataType: 'jsonp',
        success: function() { alert("Success"); },
        error: function() { alert('Failed!'); }

    });

    console.log("resquest has been sent " + commentId);
}