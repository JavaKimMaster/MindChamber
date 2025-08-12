# Selenium을 이용한 브라우저 자동화에 필요한 모듈들
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait, Select
from selenium.webdriver.support import expected_conditions as EC

# 날짜, 시간, 경로 처리용 표준 라이브러리
from datetime import datetime
import time
import os

# DB 연동, 엑셀 처리, 데이터 분석용 외부 라이브러리
import pymysql
import xlrd
import pandas as pd
from bs4 import BeautifulSoup
from io import StringIO
from sklearn.ensemble import RandomForestClassifier
from sklearn.preprocessing import MultiLabelBinarizer
from matplotlib import pyplot as plt
import numpy as np

# 크롬 드라이버 실행 시 다운로드 폴더를 현재 디렉토리로 설정
options = webdriver.ChromeOptions()
download_dir = os.getcwd()
prefs = {"download.default_directory": download_dir}
options.add_experimental_option("prefs", prefs)

# 크롬 드라이버 실행 및 대상 페이지 접속
driver = webdriver.Chrome(options=options)
driver.get("https://www.dhlottery.co.kr/gameResult.do?method=byWin")
driver.maximize_window()
wait = WebDriverWait(driver, 10)

# 현재 날짜시간을 기반으로 파일명 정의
now_str = datetime.now().strftime("%Y%m%d_%H%M%S")
final_name_1 = f"lotto_{now_str}_1_600.xls"
final_name_2 = f"lotto_{now_str}_601_1200.xls"

# 1~600회차 엑셀 다운로드 함수
def download_lotto_excel_1_600():
    try:
        # 회차 범위 드롭다운에서 1~600 선택
        select_elem = wait.until(EC.presence_of_element_located((By.ID, "hdrwComb")))
        select_box = Select(select_elem)
        select_box.select_by_value("2")
        print("✅ '1~600' 회차 선택 완료")
        time.sleep(1)
    except Exception as e:
        print(f"❌ 회차 선택 실패: {e}")

    try:
        # 시작 회차를 '1회'로 선택
        drw_start_select = Select(driver.find_element(By.ID, "drwNoStart"))
        options_list = drw_start_select.options
        drw_start_select.select_by_index(len(options_list) - 1)
        print("✅ 최초 1회 선택 완료")
    except Exception as e:
        print(f"❌ 최초 1회 선택 실패: {e}")

    try:
        # 엑셀 다운로드 버튼 클릭
        excel_btn = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.ID, "exelBtn"))
        )
        excel_btn.click()
        print("✅ 엑셀 다운로드 완료")
    except Exception as e:
        print(f"❌ 엑셀 다운로드 실패: {e}")

    time.sleep(5)  # 다운로드 대기
    default_name = "excel.xls"
    default_path = os.path.join(download_dir, default_name)
    final_path = os.path.join(download_dir, final_name_1)

    try:
        # 다운로드된 파일명을 타임스탬프 기반으로 변경
        if os.path.exists(default_path):
            os.rename(default_path, final_path)
            print(f"✅ 파일 이름 변경 완료 → {final_name_1}")
        else:
            print(f"❌ 다운로드된 원본 파일이 존재하지 않음: {default_path}")
    except Exception as e:
        print(f"❌ 파일 이름 변경 실패: {e}")

# 601~1200회차 엑셀 다운로드 함수
def download_lotto_excel_601_1200():
    try:
        select_elem = wait.until(EC.presence_of_element_located((By.ID, "hdrwComb")))
        select_box = Select(select_elem)
        select_box.select_by_value("1")
        print("✅ '601~1200' 회차 선택 완료")
        time.sleep(1)
    except Exception as e:
        print(f"❌ 회차 선택 실패(2): {e}")

    try:
        drw_start_select = Select(driver.find_element(By.ID, "drwNoStart"))
        options_list = drw_start_select.options
        drw_start_select.select_by_index(len(options_list) - 1)
        print("✅ 최초 601회 선택 완료")
    except Exception as e:
        print(f"❌ 최초 601회 선택 실패: {e}")

    try:
        excel_btn = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.ID, "exelBtn"))
        )
        excel_btn.click()
        print("✅ 엑셀 다운로드 완료")
    except Exception as e:
        print(f"❌ 엑셀 다운로드 실패: {e}")

    time.sleep(5)
    default_name = "excel.xls"
    default_path = os.path.join(download_dir, default_name)
    final_path = os.path.join(download_dir, final_name_2)

    try:
        if os.path.exists(default_path):
            os.rename(default_path, final_path)
            print(f"✅ 파일 이름 변경 완료 → {final_name_2}")
        else:
            print(f"❌ 다운로드된 원본 파일이 존재하지 않음: {default_path}")
    except Exception as e:
        print(f"❌ 파일 이름 변경 실패: {e}")

# 로또 테이블의 기존 데이터를 삭제하는 함수
def clear_lotto_result_table():
    try:
        conn = pymysql.connect(
            host='localhost', user='root', password='javakimmaster',
            db='lottodb', charset='utf8'
        )
        cursor = conn.cursor()
        cursor.execute("DELETE FROM lotto_result")
        conn.commit()
        print("🧹 기존 로또 데이터 삭제 완료")
    except Exception as e:
        print(f"❌ 기존 데이터 삭제 실패: {e}")
    finally:
        cursor.close()
        conn.close()

# HTML 파일을 읽고 파싱하여 DB에 저장
def insert_lotto_html_to_db(file_path):
    try:
        with open(file_path, 'r', encoding='euc-kr') as f:
            soup = BeautifulSoup(f, 'html.parser')
        print(f"✅ HTML 파일 열기 성공: {file_path}")
    except Exception as e:
        print(f"❌ HTML 파일 열기 실패: {e}")
        return

    try:
        # 두 번째 테이블에서 데이터 추출
        table = soup.find_all("table")[1]
        df = pd.read_html(StringIO(str(table)))[0]
        print("✅ HTML 테이블 파싱 성공")
    except Exception as e:
        print(f"❌ HTML 테이블 읽기 실패: {e}")
        return

    # 첫 행이 컬럼명이면 설정
    if df.iloc[0].str.contains("추첨일").any():
        df.columns = df.iloc[0]
        df = df[1:].reset_index(drop=True)

    try:
        # 컬럼명 강제 지정
        df.columns = [
            "draw_year", "draw_no", "draw_date", "win1", "win1_amt", "win2", "win2_amt",
            "win3", "win3_amt", "win4", "win4_amt", "win5", "win5_amt",
            "num1", "num2", "num3", "num4", "num5", "num6", "bonus"
        ]
    except Exception as e:
        print(f"❌ 컬럼명 설정 실패: {e}")
        return

    # 숫자 컬럼만 필터링
    df = df[["draw_no", "num1", "num2", "num3", "num4", "num5", "num6", "bonus"]]
    df = df[df["draw_no"].str.contains("회차") == False]

    try:
        conn = pymysql.connect(
            host='localhost', user='root', password='javakimmaster',
            db='lottodb', charset='utf8'
        )
        cursor = conn.cursor()
        print("✅ DB 연결 성공")
    except Exception as e:
        print(f"❌ DB 연결 실패: {e}")
        return

    # INSERT 쿼리 실행
    sql = """
        INSERT IGNORE INTO lotto_result
        (draw_no, num1, num2, num3, num4, num5, num6, bonus)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
    """
    try:
        for _, row in df.iterrows():
            cursor.execute(sql, (
                int(row['draw_no']), int(row['num1']), int(row['num2']),
                int(row['num3']), int(row['num4']), int(row['num5']),
                int(row['num6']), int(row['bonus'])
            ))
        conn.commit()
        print("✅ DB 저장 완료")
    except Exception as e:
        print(f"❌ INSERT 실행 중 오류: {e}")
    finally:
        cursor.close()
        conn.close()

# DB에서 로또 번호 데이터를 불러오는 함수
def load_lotto_data():
    conn = pymysql.connect(
        host='localhost', user='root', password='javakimmaster',
        db='lottodb', charset='utf8', autocommit=True
    )
    query = "SELECT draw_no, num1, num2, num3, num4, num5, num6 FROM lotto_result ORDER BY draw_no"
    df = pd.read_sql(query, conn)
    conn.close()
    return df

# 전처리: 합계와 범위를 구하고 y는 다음 회차 번호로 설정
def preprocess(df):
    df['sum'] = df[['num1','num2','num3','num4','num5','num6']].sum(axis=1)
    df['range'] = df[['num1','num2','num3','num4','num5','num6']].max(axis=1) - df[['num1','num2','num3','num4','num5','num6']].min(axis=1)
    X = df[['draw_no', 'sum', 'range']][:-1]
    y = df[['num1','num2','num3','num4','num5','num6']][1:]
    y_list = y.values.tolist()
    mlb = MultiLabelBinarizer(classes=range(1, 46))
    y_encoded = mlb.fit_transform(y_list)
    return X, y_encoded, mlb

# 랜덤 포레스트 모델 학습
def train_model(X, y):
    model = RandomForestClassifier(n_estimators=200, random_state=42)
    model.fit(X, y)
    return model

# 마지막 회차 데이터를 기반으로 추천 번호 세트 생성
def predict_multiple_sets(model, mlb, last_row, set_count=5):
    last_row_df = pd.DataFrame([last_row], columns=['draw_no', 'sum', 'range'])
    probas = model.predict_proba(last_row_df)
    prob_mean = [proba[0][1] for proba in probas]
    number_pool = list(mlb.classes_)
    probabilities = np.array(prob_mean) / sum(prob_mean)
    all_sets = []
    for _ in range(set_count):
        sampled = np.random.choice(number_pool, size=6, replace=False, p=probabilities)
        all_sets.append(sorted(sampled.tolist()))
    return all_sets, prob_mean

# 전체 실행 흐름 제어
def main():
    df = load_lotto_data()
    X, y, mlb = preprocess(df)
    model = train_model(X, y)
    last_row = df.iloc[-1][['draw_no', 'sum', 'range']].values
    all_sets, prob_mean = predict_multiple_sets(model, mlb, last_row)
    for i, num_set in enumerate(all_sets, 1):
        print(f"🎯 추천 세트 {i}: {num_set}")

# 메인 실행부
if __name__ == "__main__":
    print("-----------")
    download_lotto_excel_1_600()
    download_lotto_excel_601_1200()

    print("-----------")
    clear_lotto_result_table()

    print("-----------")
    insert_lotto_html_to_db(final_name_1)
    insert_lotto_html_to_db(final_name_2)

    print("-----------")
    main()
