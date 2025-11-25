/*NAV*/
const nav = document.querySelector('.nav');

    window.addEventListener('scroll', function(){
        nav.classList.toggle('active', window.scrollY >0)
    })


/*OVERLAY - UPDATE*/
const overlayBlock = document.getElementById("link-trigger-update");
const overlayNone = document.getElementById("overlay-update");
const menuUpdate = document.getElementById("menu-update");

if (overlayBlock && overlayNone && menuUpdate) {
  overlayBlock.addEventListener("click", function (event) {
    event.preventDefault();
    overlayNone.style.display = "block";
    menuUpdate.style.display = "block";
  });

  overlayNone.addEventListener("click", function () {
    overlayNone.style.display = "none";
    menuUpdate.style.display = "none";
  });
} else {
  console.log("Algunos elementos del overlay no existen en el DOM.");
}

/*OVERLAY - POST*/
const overlayBlockPost = document.getElementById("link-trigger-post");
const overlayNonePost = document.getElementById("overlay-post");
const menuUpdatePost = document.getElementById("menu-post");

if (overlayBlockPost && overlayNonePost && menuUpdatePost) {
  overlayBlockPost.addEventListener("click", function (event) {
    event.preventDefault();
    overlayNonePost.style.display = "block";
    menuUpdatePost.style.display = "block";
  });

  overlayNonePost.addEventListener("click", function () {
    overlayNonePost.style.display = "none";
    menuUpdatePost.style.display = "none";
  });
} else {
  console.log("Algunos elementos del overlay no existen en el DOM.");
}
