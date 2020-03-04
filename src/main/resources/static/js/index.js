var cart = {}; //моя корзина

$('document').ready(function(){
    checkCart();
    totalPrice();
});

function addToCart(productId) {

    if (cart[productId] == undefined ) {
        cart[productId] = 1;

        saveCart();

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
    localStorage.removeItem('cart');
    document.cookie = "cart= ; expires = Thu, 01 Jan 1970 00:00:00 GMT";

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

function counterChange(id, flag) {

    var att_price = document.getElementById('price-' + id);
    var att_count = document.getElementById('count-' + id);

    var count = att_count.getAttribute("value");
    var price = att_price.getAttribute("value");

    if(flag < 0) {
        if (count == 1)
            return;
        count--;
        cart[id]--;
        saveCart();
    }else {
        count++;
        cart[id]++;
        saveCart();
    }
    document.getElementById('count-' + id)
        .setAttribute("value",count);

    att_price.innerText = price * count;
    totalPrice()
}

function deleteProduct(id) {

    delete cart[id];

    var elem = document.getElementsByClassName("product_cart");
    for (var i = 0; i < elem.length; i++){
        elem.item(i).textContent = "In the cart: " + Object.keys(cart).length.toString();
    }

    saveCart();

    document.getElementById('tr_product_id_' + id).remove();

    if(Object.keys(cart).length== 0){
        document.getElementsByClassName('for_cart').item(0).remove()
        document.getElementsByClassName('row for_cart').item(0).remove();

        document.getElementById('empty_cart').style.display = 'block';
    }
    totalPrice()
}

function totalPrice() {

    var final_price = 0;

    for(var key in cart){
        var att_price = document.getElementById('price-' + key);
        var att_count = document.getElementById('count-' + key);

        var count = att_count.getAttribute("value");
        var price = att_price.getAttribute("value");

        final_price += price * count;
    }

    document.getElementById('total_price').innerText = '$' + final_price;
}

function saveCart() {
    localStorage.setItem('cart', JSON.stringify(cart));

    var str = "cart=";
    for (var key in cart){
        str += key + '-' + cart[key] + '|';
    }
    str += ';path=/';
    document.cookie = str;
}
