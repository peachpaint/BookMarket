function addToCart(bookId) {
  if (confirm("장바구니에 도서를 추가합니까?") == true) {
    document.addForm.action = "/BookMarket/cart/book/" + bookId;
    document.addForm.submit();
  }
} //선택 도서를 장바구니에 등록

function removeFromCart(bookId, cartId) {
  document.removeForm.action = "/BookMarket/cart/book/" + bookId;
  document.removeForm.submit();
  setTimeout("location.reload()", 10);
} //장바구니에 들록된 도서 항목을 삭제하는 메서드

function clearCart(cartId) {
  if (!cartId) {
    alert("장바구니가 비어있습니다.");
    return;
  }
  if (confirm("장바구니를 비우시겠습니까?") == true) {
    var form = document.clearForm;
    form.action = "/BookMarket/cart/" + cartId;
    form.submit();
  }
} //장바구니에 저장된 모든 도서항목을 삭제하는 메서드

function updateQuantity(bookId, newQuantity) {
  // 수량이 1 미만이면 1로 설정
  newQuantity = Math.max(1, parseInt(newQuantity));

  var cartId = document
    .querySelector("[data-cart-id]")
    .getAttribute("data-cart-id");

  var xhr = new XMLHttpRequest();
  xhr.open(
    "PATCH",
    "/BookMarket/cart/" +
      cartId +
      "/book/" +
      bookId +
      "/quantity?quantity=" +
      newQuantity,
    true
  );
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhr.onload = function () {
    if (xhr.status === 204) {
      // 성공 시 페이지 새로고침
      location.reload();
    } else {
      alert("수량 변경에 실패했습니다.");
    }
  };

  xhr.onerror = function () {
    alert("수량 변경 중 오류가 발생했습니다.");
  };

  xhr.send();
} //장바구니 도서 수량 변경 메서드
