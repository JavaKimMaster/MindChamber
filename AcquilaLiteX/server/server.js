// 📌 외부에서 가져온 도구들(import)
const express = require("express");      // 웹 서버를 쉽게 만들 수 있게 도와주는 도구
const bodyParser = require("body-parser"); // 요청에서 넘어온 JSON 데이터를 쉽게 읽게 해줌
const cors = require("cors");            // 다른 주소에서 요청할 때 에러 안 나게 해주는 보호막 해제 도구
const mysql = require("mysql2");         // MariaDB와 연결해서 데이터를 읽고 쓰게 해주는 도구
const axios = require("axios");          // 외부 API (예: 빗썸)에서 데이터를 가져오는 도구

// 📌 서버 앱 만들기
const app = express();                   // 서버 프로그램의 시작
const PORT = 3000;                       // 서버가 열릴 포트 번호 (http://localhost:3000)

// 📌 미들웨어 설정 (요청 받을 때 도와주는 도구들)
app.use(cors());                         // CORS 문제 해결 (다른 사이트에서 접근 가능하게 함)
app.use(bodyParser.json());              // 요청에 담긴 JSON 데이터를 꺼내 쓸 수 있게 함
app.use(express.static("public"));       // public 폴더 안의 파일(HTML 등)을 자동으로 보여줌


const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const SECRET_KEY = "your_jwt_secret_key"; // 나중에 .env로 분리 권장


// 📌 MariaDB 연결 설정
const db = mysql.createConnection({
  host: "localhost",                     // 데이터베이스 주소 (내 컴퓨터니까 localhost)
  port:"3306",
  user: "root",                          // 사용자 이름 (기본은 root)
  password: "javakimmaster",             // 본인이 설정한 비밀번호
  database: "aquilalitex"                // 사용할 데이터베이스 이름
});



// ✅ 활동 로그 저장 함수
function saveLog(username, action) {
  db.query(
    "INSERT INTO activity_log (username, action) VALUES (?, ?)",
    [username, action],
    (err) => {
      if (err) console.error("로그 저장 실패:", err);
    }
  );
}



// ✅ 1. 빗썸에서 실시간 코인 시세 가져오는 API
app.get("/api/prices", async (req, res) => {
  try {
    console.log('빗썸에서 실시간 코인 시세 가져오는 API 호출되었음')
    
    // axios를 이용해 빗썸에서 전체 코인 가격 정보 가져오기
    const response = await axios.get("https://api.bithumb.com/public/ticker/ALL_KRW");

    // 응답 받은 데이터 중 'data' 부분만 꺼냄
    const data = response.data.data;

    // Object.entries(): key-value 쌍을 배열로 만듦 → 각 코인을 필터링
    const filtered = Object.entries(data)
      .filter(([key, value]) => typeof value === "object") // 숫자 말고 코인 데이터만 걸러냄
      .map(([key, value]) => ({
        coin: key,                   // 코인 이름 (예: BTC)
        price: value.closing_price  // 현재 가격 (종가)
      }));

    // 클라이언트(브라우저)에게 시세 정보 전달
    res.json(filtered);
  } catch (error) {
    // 에러가 나면 사용자에게 실패 메시지 전달
    res.status(500).send("시세 조회 실패");
  }
});

// ✅ 2. 사용자가 즐겨찾기한 코인 DB에 저장하는 API
app.post("/api/favorites", (req, res) => {
  const { coin } = req.body;  // 요청에서 보낸 'coin' 값을 꺼냄
  const username = req.user.username;

  // DB에 INSERT (추가)
  db.query("INSERT INTO favorites (coin) VALUES (?)", [coin], (err) => {
    if (err) return res.status(500).send("즐겨찾기 저장 실패"); // 에러 처리
    saveLog(username, `즐겨찾기 추가: ${coin}`); // 📌 로그 기록
    res.send("즐겨찾기 등록 완료");  // 성공 메시지
  });
});

// ✅ 3. 저장된 즐겨찾기 목록을 가져오는 API
app.get("/api/favorites", (req, res) => {
  // DB에서 모든 즐겨찾기 코인 조회
  db.query("SELECT coin FROM favorites", (err, rows) => {
    if (err) return res.status(500).send("즐겨찾기 조회 실패"); // 에러 처리
    res.json(rows); // 결과를 JSON 형태로 사용자에게 전달
  });
});

// ✅ 4. 즐겨찾기에서 코인 삭제하는 API
app.delete("/api/favorites/:coin", (req, res) => {
  const coin = req.params.coin; // 주소에 들어온 코인 이름 추출
  const username = req.user.username;

  // DB에서 해당 코인 삭제
  db.query("DELETE FROM favorites WHERE coin = ?", [coin], (err) => {
    if (err) return res.status(500).send("즐겨찾기 삭제 실패"); // 에러 처리
    saveLog(username, `즐겨찾기 삭제: ${coin}`); // 📌 로그 기록
    res.send("삭제 완료"); // 성공 메시지
  });
});

// ✅ 5. 서버를 실행시킴
app.listen(PORT, () => {
  console.log(`🚀 AquilaLiteX 백엔드 실행 중 → http://localhost:${PORT}`);
});



// ✅ 회원가입 API 엔드포인트
// 클라이언트에서 POST 요청을 /api/register로 보냈을 때 실행되는 핸들러
app.post("/api/register", async (req, res) => {

  // 요청 본문에서 username과 password를 추출
  const { username, password } = req.body;

  try {
    // 사용자가 입력한 비밀번호를 해시(암호화)함. 두 번째 인자 10은 saltRounds (보안 수준)
    const hashed = await bcrypt.hash(password, 10);

    // DB에 사용자 정보 삽입 (username과 암호화된 password)
    db.query(
      "INSERT INTO users (username, password) VALUES (?, ?)", // ?는 바인딩 자리
      [username, hashed], // 실제 바인딩할 값
      (err) => {
        // 만약 쿼리 중 에러가 발생했다면
        if (err) {

          // 에러가 "중복된 아이디"라면 클라이언트에 400 응답
          if (err.code === "ER_DUP_ENTRY") {
            return res.status(400).json({ message: "이미 존재하는 사용자입니다." });
          }

          // 다른 DB 에러인 경우 500 서버 오류로 응답
          return res.status(500).json({ message: "회원가입 실패" });
        }

        // 에러 없이 회원가입 성공 시 201 상태코드와 메시지를 응답
        res.status(201).json({ message: "회원가입 완료" });
      }
    );
  } catch {
    // bcrypt.hash 중 에러가 발생했을 경우 500 서버 오류 응답
    res.status(500).json({ message: "서버 오류" });
  }
});




// ✅ 로그인 API
// 클라이언트가 /api/login으로 POST 요청을 보내면 실행되는 라우터 핸들러
app.post("/api/login", (req, res) => {

  // 요청 본문(req.body)에서 username과 password를 추출
  const { username, password } = req.body;

  // DB에서 해당 username을 가진 사용자 조회
  db.query("SELECT * FROM users WHERE username = ?", [username], async (err, results) => {

    // 에러가 있거나 사용자 정보가 없으면 "사용자 없음" 오류 응답 (401: Unauthorized)
    if (err || results.length === 0) return res.status(401).json({ message: "사용자 없음" });

    // 조회된 사용자 정보를 user 변수에 저장
    const user = results[0];

    // 입력한 비밀번호와 DB에 저장된 암호화된 비밀번호를 비교
    const isMatch = await bcrypt.compare(password, user.password);

    // 비밀번호가 일치하지 않으면 "비밀번호 오류" 응답
    if (!isMatch) return res.status(401).json({ message: "비밀번호 오류" });

    // 비밀번호까지 일치했다면 JWT 토큰 생성
    const token = jwt.sign(
      { username: user.username }, // 토큰에 담을 사용자 정보 (페이로드)
      SECRET_KEY,                  // 서명용 비밀 키
      { expiresIn: "1h" }          // 토큰 유효 시간: 1시간
    );

    saveLog(username, "로그인"); // 📌 로그인 성공 시 로그

    // 생성된 토큰을 클라이언트에게 JSON으로 응답
    res.json({ token });
  });
});


// ✅ 인증 미들웨어
// 로그인한 사용자만 접근 가능한 API에서 토큰을 검증하는 역할을 함
function authenticate(req, res, next) {

  // 요청 헤더에서 Authorization 값을 꺼냄 (예: "Bearer 토큰값")
  const authHeader = req.headers.authorization;

  // Authorization 헤더가 없으면 인증되지 않은 요청으로 간주 → 401 Unauthorized 응답
  if (!authHeader) return res.status(401).json({ message: "토큰 없음" });

  // "Bearer 토큰값" 형식에서 공백 기준으로 나눠 두 번째 요소(토큰값)만 추출
  const token = authHeader.split(" ")[1];

  try {
    // 추출한 토큰을 검증 (서명이 맞고 만료되지 않았는지 확인)
    const decoded = jwt.verify(token, SECRET_KEY);

    // 검증된 사용자 정보를 요청 객체에 추가하여 이후 미들웨어/라우터에서 사용 가능하게 함
    req.user = decoded;

    // 인증이 성공했으므로 다음 미들웨어 또는 라우터로 진행
    next();
  } catch {
    // 토큰이 유효하지 않거나 만료되었을 경우 → 401 Unauthorized 응답
    res.status(401).json({ message: "토큰 오류" });
  }
}


// ✅ 로그인한 사용자 정보 가져오기 API
// 클라이언트가 토큰을 가지고 요청할 때 해당 사용자 정보를 반환함
// 인증 미들웨어 authenticate를 먼저 거쳐야 이 라우터에 도달 가능
app.get("/api/me", authenticate, (req, res) => {

  // 인증 미들웨어에서 req.user에 사용자 정보가 저장되어 있으므로,
  // 그 중 username을 꺼내서 JSON 형태로 응답
  res.json({ user: req.user.username });
});



// ✅ 활동 로그 조회 API
// 로그인한 사용자가 자신의 활동 로그를 확인할 수 있도록 정보를 반환하는 라우터
app.get("/api/logs", authenticate, (req, res) => {

  // 인증 미들웨어(authenticate)를 통과했기 때문에,
  // req.user.username에 로그인된 사용자 이름이 들어 있음
  const username = req.user.username;

  // activity_log 테이블에서 해당 사용자의 로그를 시간 역순으로 조회하는 SQL 실행
  db.query(
    "SELECT action, created_at FROM activity_log WHERE username = ? ORDER BY created_at DESC",
    [username], // ?에 바인딩할 실제 값 (로그인된 사용자명)

    // SQL 쿼리 실행 결과를 처리하는 콜백 함수
    (err, rows) => {
      // 에러 발생 시 500 오류 응답
      if (err) return res.status(500).json({ message: "로그 조회 실패" });

      // 성공적으로 로그를 가져왔을 경우 JSON 형식으로 응답
      res.json(rows);
    }
  );
});


