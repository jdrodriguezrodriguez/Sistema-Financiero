/*OVERLAY - PASS*/
const overlayBlock = document.getElementById("forgot-password");
const overlayNone = document.getElementById("overlay-password");
const menuPassword = document.getElementById("menu-password");

if (overlayBlock && overlayNone && menuPassword) {
  overlayBlock.addEventListener("click", function (event) {
    event.preventDefault();
    overlayNone.style.display = "block";
    menuPassword.style.display = "block";
  });

  overlayNone.addEventListener("click", function () {
    overlayNone.style.display = "none";
    menuPassword.style.display = "none";
  });
} else {
  console.log("Algunos elementos del overlay no existen en el DOM.");
}

/*OVERLAY - USER*/
const overlayBlockUser = document.getElementById("forgot-username");
const overlayNoneUser = document.getElementById("overlay-username");
const menuPasswordUser = document.getElementById("menu-username");

if (overlayBlockUser && overlayNoneUser && menuPasswordUser) {
  overlayBlockUser.addEventListener("click", function (event) {
    event.preventDefault();
    overlayNoneUser.style.display = "block";
    menuPasswordUser.style.display = "block";
  });

  overlayNoneUser.addEventListener("click", function () {
    overlayNoneUser.style.display = "none";
    menuPasswordUser.style.display = "none";
  });
} else {
  console.log("Algunos elementos del overlay no existen en el DOM.");
}