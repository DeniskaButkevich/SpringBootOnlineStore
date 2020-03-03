var cart = {}; //моя корзина

$('document').ready(function(){
    checkCart();
});
function presF() {
    cart[656] = 777;
    localStorage.setItem('cart', JSON.stringify(cart));
    $.ajax({
        type : "POST",
        url : "/filter",
        data : {
            cart : JSON.parse(localStorage.getItem("cart"))

        }, // parameters
        success : function() {
            alert('Load was performed.');
        }
    });

}
function addToCart(productId) {

    if (cart[productId] == undefined ) {
        cart[productId] = 1;
        localStorage.setItem('cart', JSON.stringify(cart));


        var elem = document.getElementsByClassName("product_cart");
        for (var i = 0; i < elem.length; i++){
            elem.item(i).textContent = "In the cart: " + Object.keys(cart).length.toString();
        }

        var addedElem = document.getElementsByClassName(productId);
        for (var i = 0; i < addedElem.length; i++){
            addedElem.item(i).parentElement
                .setAttribute('onclick','location.href=\'/cart\'');
            addedElem.item(i).textContent  = "Added";
        }

        document.getElementById("scroll-cart").style.display = "block";
    }
}

function removeFromCart() {

    for (var key in cart){
        delete cart[key];

        var addedElem = document.getElementsByClassName(key);
        for (var i = 0; i < addedElem.length; i++){
            addedElem.item(i).parentElement
                .setAttribute('onclick','addToCart(' + key +')');
            addedElem.item(i).textContent = "Add to Cart";
        }
    }

    var elem = document.getElementsByClassName("product_cart");
    for (var i = 0; i < elem.length; i++){
        elem.item(i).textContent = "";
    }

    cart = {};
    localStorage.setItem('cart', JSON.stringify(cart));
    document.getElementById("scroll-cart").style.display = "none";
}

function checkCart(){
    //проверяю наличие корзины в localStorage;
    if (localStorage.getItem('cart') != null
        && localStorage.getItem('cart').length != 2) {
        cart = JSON.parse (localStorage.getItem('cart'));

        var elem = document.getElementsByClassName("product_cart");
        for (var i = 0; i < elem.length; i++){
            elem.item(i).textContent = "In the cart: " + Object.keys(cart).length.toString();
        }

        document.getElementById("scroll-cart").style.display = "block";

        for (var key in cart){

            var addedElem = document.getElementsByClassName(key);
            for (var i = 0; i < addedElem.length; i++){
                addedElem.item(i).parentElement
                    .setAttribute('onclick','location.href=\'/cart\'');
                addedElem.item(i).textContent  = "Added";
            }
        }
    }


}