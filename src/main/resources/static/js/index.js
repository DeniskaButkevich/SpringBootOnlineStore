var cart = {};
var compare = new Array();
var wishlist = new Array();

$('document').ready(function () {
    checkCart();
    checkCompare();
    checkWishlist();
    if (window.location.pathname == '/cart') {
        totalPrice();
    }
    if (window.location.pathname == '/compare-products') {
        checkEmptyCompare();
    }
});

function addToCart(productId) {

    if (cart[productId] == undefined) {
        cart[productId] = 1;

        saveCart();

        var elem = document.getElementsByClassName("product_cart");
        for (var i = 0; i < elem.length; i++) {
            elem.item(i).textContent = "In the cart: " + Object.keys(cart).length.toString();
        }

        var addedElem = document.getElementsByClassName(productId);
        for (var i = 0; i < addedElem.length; i++) {
            addedElem.item(i).parentElement
                .setAttribute('onclick', 'location.href=\'/cart\'');
            addedElem.item(i).textContent = "Added";
        }

        document.getElementById("scroll-cart").style.display = "block";
    }
}

function removeFromCart() {
    for (var key in cart) {
        delete cart[key];
        var addedElem = document.getElementsByClassName(key);
        for (var i = 0; i < addedElem.length; i++) {
            addedElem.item(i).parentElement
                .setAttribute('onclick', 'addToCart(' + key + ')');
            addedElem.item(i).textContent = "Add to Cart";
        }
    }
    var elem = document.getElementsByClassName("product_cart");
    for (var i = 0; i < elem.length; i++) {
        elem.item(i).textContent = "";
    }
    cart = {};
    document.cookie = "cart= ; expires = Thu, 01 Jan 1970 00:00:00 GMT; path=/";
    document.getElementById("scroll-cart").style.display = "none";
}

function checkCart() {

    if (document.cookie.includes('cart=')) {

        var cookie_elem = getCookie('cart').split('|');

        for (var i = 0; i < cookie_elem.length; i++) {
            var cart_item = cookie_elem[i].split('-').map(x=>+x);
            cart[cart_item[0]]= cart_item[1];
        }

        var elem = document.getElementsByClassName("product_cart");
        for (var i = 0; i < elem.length; i++) {
            elem.item(i).textContent = "In the cart: " + Object.keys(cart).length.toString();
        }

        document.getElementById("scroll-cart").style.display = "block";

        for (var key in cart) {

            var addedElem = document.getElementsByClassName(key);
            for (var i = 0; i < addedElem.length; i++) {
                addedElem.item(i).parentElement
                    .setAttribute('onclick', 'location.href=\'/cart\'');
                addedElem.item(i).textContent = "Added";
            }
        }
    }
}

function saveCart() {

    var str = "cart=";
    for (var key in cart) {
        str += key + '-' + cart[key] + '|';
    }
    str = str.slice(0, -1);
    str += ';path=/';
    document.cookie = str;
}

function deleteProduct(id) {

    delete cart[id];

    var elem = document.getElementsByClassName("product_cart");
    for (var i = 0; i < elem.length; i++) {
        elem.item(i).textContent = "In the cart: " + Object.keys(cart).length.toString();
    }

    saveCart();

    document.getElementById('tr_product_id_' + id).remove();

    if (Object.keys(cart).length == 0) {
        document.getElementsByClassName('for_cart').item(0).remove()
        document.getElementsByClassName('row for_cart').item(0).remove();

        document.getElementById('empty_cart').style.display = 'block';
    }
    totalPrice()
}

function deleteElementCompare(productId) {

    var index = compare.indexOf(productId);
    compare.splice(index, 1)

        if (compare.length < 1) {
            document.getElementById("scroll-compare").style.display = "none";
            compare = [];
            document.cookie = "compare= ; expires = Thu, 01 Jan 1970 00:00:00 GMT; path=/";

            var container =  document.getElementById('compare-container');
            if(container != null){
                container.remove()
                var empty_container = document.getElementById('compare-container-empty');
                empty_container.style.display = 'block';
                return;
            }

        } else {
            document.getElementById("scroll-compare").style.display = "block";

            var classes = document.getElementsByClassName('compare-td-' + productId);
            var length_classes = classes.length;
            for (var i = 0; i < length_classes; i++) {
                classes.item(0).remove();
            }
            saveCookie('compare=', compare);
            var elem = document.getElementById('product_compare');
            elem.textContent = "In the compare: " + compare.length.toString();
        }
}

function deleteElementWishlist(productId) {

    var index = wishlist.indexOf(productId);
    wishlist.splice(index, 1)

    if (wishlist.length < 1) {
        wishlist = [];
        document.cookie = "wishlist= ; expires = Thu, 01 Jan 1970 00:00:00 GMT; path=/";

        var container =  document.getElementById('wishlist-container');
        if(container != null){
            container.remove()
            var empty_container = document.getElementById('empty_wishlist');
            empty_container.style.display = 'block';
            return;
        }

    } else {

        var elem = document.getElementById('tr_product_id_' + productId);
        elem.remove();
        saveCookie('wishlist=', wishlist);
    }
}

function checkEmptyCompare(){
    if (compare.length < 1) {
        var container =  document.getElementById('compare-container');
        if(container != null){
            container.remove()
            var empty_container = document.getElementById('compare-container-empty');
            empty_container.style.display = 'block';
        }
    }
}

function counterChange(id, flag) {

    var att_price = document.getElementById('price-' + id);
    var att_count = document.getElementById('count-' + id);

    var count = att_count.getAttribute("value");
    var price = att_price.getAttribute("value");

    if (flag < 0) {
        if (count == 1)
            return;
        count--;
        cart[id]--;
        saveCart();
    } else {
        count++;
        cart[id]++;
        saveCart();
    }
    document.getElementById('count-' + id)
        .setAttribute("value", count);

    att_price.innerText = price * count;
    totalPrice()
}

////////////////////////////////////////////////

function totalPrice() {

    if (document.cookie.includes('cart=')) {
        var final_price = 0;

        for (var key in cart) {
            var att_price = document.getElementById('price-' + key);
            var att_count = document.getElementById('count-' + key);
            if (att_price == null || att_count == null)
                return;
            var count = cart[key];
            var price = att_price.getAttribute("value");

            final_price += price * count;

            document.getElementById('count-' + key)
                .setAttribute("value", count);

            att_price.innerText = price * count;
        }
        if (document.getElementById('total_price') != null) {
            document.getElementById('total_price').innerText = '$' + final_price;
        }
    }
}

function collapsePanel(id_close, id_open) {
    document.getElementById(id_close).click();
    document.getElementById(id_open).click();
    setTimeout(function () {

    }, 5000);
}

////////////////////////////////////////////////

function addToWishlist(productId) {
    if (!wishlist.includes(productId)) {
        wishlist.push(productId);

        var addedElem = document.getElementById('like-' + productId);
        if (addedElem != null) {
            addedElem.classList.remove('icon-like');
            addedElem.classList.add('icon-like-active');
        }
    } else {
        var index = wishlist.indexOf(productId);
        wishlist.splice(index, 1)

        var addedElem = document.getElementById('like-' + productId);
        if (addedElem != null) {
            addedElem.classList.remove('icon-like-active');
            addedElem.classList.add('icon-like');
        }
        if (wishlist.length < 1) {
            wishlist = [];
            document.cookie = "wishlist= ; expires = Thu, 01 Jan 1970 00:00:00 GMT; path=/";
            return;
        }

    }
    saveCookie('wishlist=', wishlist);
}

function addToCompare(productId) {
    if (!compare.includes(productId)) {
        compare.push(productId);

        var addedElem = document.getElementById('compare-' + productId);
        if(addedElem != null){
            addedElem.classList.remove('icon-compare');
            addedElem.classList.add('icon-compare-active');
        }
    } else {
        var index = compare.indexOf(productId);
        compare.splice(index, 1)

        var addedElem = document.getElementById('compare-' + productId);
        if(addedElem != null){
            addedElem.classList.remove('icon-compare-active');
            addedElem.classList.add('icon-compare');
        }
    }
    saveCookie('compare=', compare);

    if (compare.length < 1) {
        document.getElementById("scroll-compare").style.display = "none";
        compare = [];
        document.cookie = "compare= ; expires = Thu, 01 Jan 1970 00:00:00 GMT; path=/";
        return;
    } else {
        document.getElementById("scroll-compare").style.display = "block";
    }
    var elem = document.getElementById('product_compare');
    elem.textContent = "In the compare: " + compare.length.toString();
}

function checkWishlist() {
    if (document.cookie.includes('wishlist=')) {

        wishlist = getCookie('wishlist').split('|').map(x=>+x);

        for (var i = 0; i < wishlist.length; i++) {
            var elem = document.getElementById('like-' + wishlist[i]);
            if (elem != null) {
                elem.classList.remove('icon-like');
                elem.classList.add('icon-like-active');
            }
        }
    }
}

function checkCompare() {
    if (document.cookie.includes('compare=')) {

        compare = getCookie('compare').split('|').map(x=>+x);

        var elem = document.getElementById("product_compare");
        if (elem != null) {
            elem.textContent = "In the compare: " + compare.length.toString();
        }

        document.getElementById("scroll-compare").style.display = "block";

        for (var i = 0; i < compare.length; i++) {
            var elem = document.getElementById('compare-' + compare[i]);
            if (elem != null) {
                elem.classList.remove('icon-compare');
                elem.classList.add('icon-compare-active');
            }
        }
    }
}

function clearWishlist() {
    for (var i = 0; i < wishlist.length; i++) {
        var addedElem = document.getElementById('like-' + compare[i]);
        addedElem.classList.remove('icon-like-active');
        addedElem.classList.add('icon-like');
    }
    wishlist = [];
    document.cookie = "wishlist= ; expires = Thu, 01 Jan 1970 00:00:00 GMT; path=/";
}

function clearCompare() {

    for (var i = 0; i < compare.length; i++) {
        var addedElem = document.getElementById('compare-' + compare[i]);
        if(addedElem != null){
            addedElem.classList.remove('icon-compare-active');
            addedElem.classList.add('icon-compare');
        }
    }
    compare = [];
    document.cookie = "compare= ; expires = Thu, 01 Jan 1970 00:00:00 GMT; path=/";

    document.getElementById("scroll-compare").style.display = "none";
}

function saveCookie(str, arr) {
    for (var i = 0; i < arr.length; i++) {
        str += arr[i];
        if (i !== arr.length - 1) {
            str += '|';
        }
    }
    str += ';path=/';
    document.cookie = str;
}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function clearPriceFilter() {
    
}
