// 📌 이 코드는 문서(DOM)가 모두 준비되면 실행됩니다.
// $(function () { ... })는 jQuery의 축약된 '문서 준비 완료' 표현입니다.
$(function () {

  // ✅ "시세 조회" 버튼을 클릭했을 때 실행되는 함수
  $("#load").click(function () {

    console.log('"시세 조회" 버튼을 클릭했을 때 실행되는 함수를 클릭 하였음')

    // 1️⃣ 서버에 GET 요청 → 빗썸에서 코인 시세를 받아오도록 백엔드에 요청
    // 서버에 GET 요청을 보낸다 → "/api/prices"
    // 그 결과(응답 데이터)를 'data'라는 이름으로 받아서,
    // 아래의 함수를 실행해라.
    $.get("/api/prices", function (data) {

      console.log('서버에 GET 요청 → 빗썸에서 코인 시세를 받아오도록 백엔드에 요청')

      // 2️⃣ 화면에 이전 결과가 남아있을 수 있으므로, 먼저 결과 영역을 비움
      $("#result").empty();

      // 3️⃣ 서버에서 받아온 코인 시세 배열(data)을 하나씩 반복 처리
      data.forEach(function (item) {

        // 4️⃣ 각 코인에 대해 이름, 가격, 즐겨찾기 버튼을 포함한 HTML 만들기
        const row = `
          <div>
            <strong>${item.coin}</strong> : ${item.price}원
            <button class="favorite-btn" data-coin="${item.coin}">
              ⭐ 즐겨찾기
            </button>
          </div>
        `;

        // 5️⃣ 만든 HTML을 <div id="result"> 안에 추가해서 사용자에게 표시
        $("#result").append(row);
      });

      // 6️⃣ 모든 "즐겨찾기" 버튼에 대해 클릭 이벤트를 등록
      // (이벤트는 시세를 불러온 후에만 버튼이 생성되므로 이 안에서 바인딩해야 함)
      // 서버 벡앤드로부터 받고 프론트엔드로 데이터를 보낼 때 만든 HTML에 있는 버튼
      $(".favorite-btn").click(function () {

        // 7️⃣ 클릭된 버튼에서 코인 이름(데이터 속성)을 꺼냄
        const coinName = $(this).data("coin");

        // 8️⃣ 서버에 POST 요청 → 코인을 즐겨찾기 DB에 저장 요청
        $.ajax({
          url: "/api/favorites",              // 즐겨찾기 저장 API 주소
          method: "POST",                     // POST 방식으로 요청
          contentType: "application/json",    // 보낼 데이터는 JSON임을 명시
          data: JSON.stringify({ coin: coinName }), // 보내는 실제 데이터: { coin: "BTC" }

          // 9️⃣ 저장 성공 시 메시지 표시
          success: function () {
            alert(`✅ ${coinName} 저장 완료!`);
          },

          // 🔟 저장 실패 시 에러 메시지 표시
          error: function () {
            alert(`❌ ${coinName} 저장 실패...`);
          }
        });
      });



    });
  });

  // ✅ 즐겨찾기 목록 보기 버튼 클릭
  $("#view-favorites").click(function () {

    // 📌 서버에 GET 요청 → DB에 저장된 즐겨찾기 코인 목록 받아오기
    $.get("/api/favorites", function (data) {
      $("#result").empty();  // 기존 결과 영역 비우기

      // 📌 즐겨찾기 코인이 하나도 없을 경우 메시지 출력
      if (data.length === 0) {
        $("#result").append("<p>즐겨찾기한 코인이 없습니다.</p>");
      }

      // 📌 코인별로 화면에 출력 + 삭제 버튼 생성
      data.forEach(function (item) {
        const row = `
          <div>
            <strong>${item.coin}</strong>
            <button class="delete-btn" data-coin="${item.coin}">❌ 삭제</button>
          </div>
        `;
        $("#result").append(row);
      });

      // 📌 동적으로 생성된 "삭제" 버튼에 클릭 이벤트 등록
      $(".delete-btn").click(function () {
        const coinName = $(this).data("coin");

        // 📌 서버에 DELETE 요청 → 해당 코인을 DB에서 삭제
        $.ajax({
          url: `/api/favorites/${coinName}`,  // 삭제할 코인을 URL에 포함
          method: "DELETE",                   // HTTP DELETE 방식
          success: function () {
            alert(`🗑 ${coinName} 삭제 완료`);
            $("#view-favorites").click();     // 목록 다시 불러오기 (새로고침처럼 동작)
          },
          error: function () {
            alert(`❌ ${coinName} 삭제 실패`);
          }
        });
      });
    });
  });



    // 모든 AJAX 요청에 토큰 자동 삽입 (로그인 후)
  $.ajaxSetup({
    beforeSend: function (xhr) {
      const token = localStorage.getItem("token");
      if (token) {
        xhr.setRequestHeader("Authorization", "Bearer " + token);
      }
    }
  });




  // ✅ 회원가입 버튼이 클릭되었을 때 실행되는 함수
  $("#register-btn").click(function () {

    // 사용자 입력창에서 아이디와 비밀번호 값을 가져옴
    const username = $("#reg-username").val();
    const password = $("#reg-password").val();

    // 서버로 회원가입 요청을 보내는 AJAX 호출
    $.ajax({
      url: "/api/register",             // 요청을 보낼 백엔드 API 경로
      method: "POST",                   // POST 방식으로 회원가입 요청
      contentType: "application/json",  // 요청 본문은 JSON 형식임을 명시
      data: JSON.stringify({ username, password }), // 아이디와 비밀번호를 JSON 문자열로 변환해서 전송

      // 서버 응답이 성공(2xx)일 경우 실행되는 함수
      success: function (res) {
        alert("✅ 회원가입 성공");      // 사용자에게 성공 알림 표시
      },

      // 서버 응답이 실패(4xx/5xx)일 경우 실행되는 함수
      error: function (res) {
        // 서버가 보낸 오류 메시지를 꺼내서 알림으로 표시
        alert("❌ 회원가입 실패: " + res.responseJSON.message);
      }
    });
  });


  // ✅ 로그인 버튼이 클릭되었을 때 실행되는 함수
  $("#login-btn").click(function () {

    // 사용자 입력창에서 아이디와 비밀번호 값을 가져옴
    const username = $("#login-username").val();
    const password = $("#login-password").val();

    // 서버에 로그인 요청을 보내는 AJAX 호출
    $.ajax({
      url: "/api/login",                 // 로그인 API 경로
      method: "POST",                   // POST 방식으로 로그인 요청
      contentType: "application/json",  // 요청 본문은 JSON 형식
      data: JSON.stringify({ username, password }), // 입력된 아이디와 비밀번호를 JSON 문자열로 변환

      // 서버 응답이 성공(2xx)일 경우 실행되는 콜백 함수
      success: function (res) {

        // 받은 JWT 토큰을 브라우저의 localStorage에 저장 (로그인 상태 유지용)
        localStorage.setItem("token", res.token);

        // 사용자에게 로그인 성공 알림 표시
        alert("✅ 로그인 성공");

        // 로그인된 사용자 정보를 가져오기 위한 추가 요청
        $.ajax({
          url: "/api/me",                 // 로그인된 사용자 정보를 확인하는 API
          headers: {
            Authorization: "Bearer " + res.token  // Authorization 헤더에 토큰을 추가 (JWT 인증 방식)
          },
          success: function (me) {
            // 서버로부터 받은 사용자 이름을 화면에 출력
            $("#login-result").text(`👤 현재 사용자: ${me.user}`);
          }
        });
      },

      // 서버 응답이 실패(4xx/5xx)일 경우 실행되는 콜백 함수
      error: function (res) {
        // 서버가 보낸 오류 메시지를 사용자에게 알림으로 표시
        alert("❌ 로그인 실패: " + res.responseJSON.message);
      }
    });
  });


  // ✅ 활동 로그 보기 버튼 클릭 시
  $("#view-log").click(function () {
    fetch("/api/logs", {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    })
      .then(res => {
        if (!res.ok) throw new Error("로그 조회 실패");
        return res.json();
      })
      .then(logs => {
        $("#result").empty(); // 기존 결과 지우기
        if (logs.length === 0) {
          $("#result").append("<p>🕸 활동 내역이 없습니다.</p>");
          return;
        }

        logs.forEach(log => {
          const time = new Date(log.created_at).toLocaleString();
          $("#result").append(`<div>🕓 ${time} - ${log.action}</div>`);
        });
      })
      .catch(err => {
        alert("❌ 활동 로그를 불러오는 데 실패했습니다.");
        console.error(err);
      });
  });


});
