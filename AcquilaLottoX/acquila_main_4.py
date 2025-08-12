from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait, Select
from selenium.webdriver.support import expected_conditions as EC
import time
import os
from datetime import datetime

import pymysql
# import openpyxl    # xlsx only
import xlrd          # xls file



import pandas as pd
import pymysql


# # 크롬 옵션: 다운로드 폴더 지정
# options = webdriver.ChromeOptions()
# download_dir = os.getcwd()
# prefs = {"download.default_directory": download_dir}
# options.add_experimental_option("prefs", prefs)

# # 드라이버 실행
# driver = webdriver.Chrome(options=options)
# driver.get("https://www.dhlottery.co.kr/gameResult.do?method=byWin")

# # ✅ 전체 화면으로 전환
# driver.maximize_window()

# # 명시적 대기 설정
# wait = WebDriverWait(driver, 10)

# # 현재 시간 획득
# now_str = datetime.now().strftime("%Y%m%d_%H%M%S")
# final_name_1 = f"lotto_{now_str}_1_600.xls"
# final_name_2 = f"lotto_{now_str}_601_1200.xls"



def download_lotto_excel_1_600():


    # ✅ 1. 회차 콤보박스 선택
    try:
        select_elem = wait.until(EC.presence_of_element_located((By.ID, "hdrwComb")))
        select_box = Select(select_elem)
        select_box.select_by_value("2")  # value="2" → "1~600"
        print("✅ '1~600' 회차 선택 완료")
        time.sleep(1)
    except Exception as e:
        print(f"❌ 회차 선택 실패: {e}")

    # 2. drwNoStart에서 가장 아래 옵션 선택 (value=1)
    try:
        drw_start_select = Select(driver.find_element(By.ID, "drwNoStart"))
        options_list = drw_start_select.options
        drw_start_select.select_by_index(len(options_list) - 1)  # 마지막 index 선택
        print("✅ 최초 1회 선택 완료")
    except Exception as e:
        print(f"❌ 최초 1회 선택 실패: {e}")

    # 3. 엑셀 다운로드 버튼 클릭
    try:
        excel_btn = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.ID, "exelBtn"))
        )
        excel_btn.click()
        print("✅ 엑셀 다운로드 완료")
    except Exception as e:
        print(f"❌ 엑셀 다운로드 실패: {e}")

    # ✅ 다운로드 대기
    time.sleep(5)
    # driver.quit()

    # ✅ 다운로드된 파일명 변경
    default_name = "excel.xls"  # 실제 다운로드 파일 이름


    default_path = os.path.join(download_dir, default_name)
    final_path = os.path.join(download_dir, final_name_1)

    try:
        if os.path.exists(default_path):
            os.rename(default_path, final_path)
            print(f"✅ 파일 이름 변경 완료 → {final_name_1}")
        else:
            print(f"❌ 다운로드된 원본 파일이 존재하지 않음: {default_path}")
    except Exception as e:
        print(f"❌ 파일 이름 변경 실패: {e}")



def download_lotto_excel_601_1200():


    # ✅ 1. 회차 콤보박스 선택
    try:
        select_elem = wait.until(EC.presence_of_element_located((By.ID, "hdrwComb")))
        select_box = Select(select_elem)
        select_box.select_by_value("1")  # value="1" → "601~1200"
        print("✅ '601~1200' 회차 선택 완료")
        time.sleep(1)
    except Exception as e:
        print(f"❌ 회차 선택 실패(2): {e}")

    # 2. drwNoStart에서 가장 아래 옵션 선택 (value=1)
    try:
        drw_start_select = Select(driver.find_element(By.ID, "drwNoStart"))
        options_list = drw_start_select.options
        drw_start_select.select_by_index(len(options_list) - 1)  # 마지막 index 선택
        print("✅ 최초 601회 선택 완료")
    except Exception as e:
        print(f"❌ 최초 601회 선택 실패: {e}")


    # 3. 엑셀 다운로드 버튼 클릭
    try:
        excel_btn = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.ID, "exelBtn"))
        )
        excel_btn.click()
        print("✅ 엑셀 다운로드 완료")
    except Exception as e:
        print(f"❌ 엑셀 다운로드 실패: {e}")

    # ✅ 다운로드 대기
    time.sleep(5)
    # driver.quit()

    # ✅ 다운로드된 파일명 변경
    default_name = "excel.xls"  # 실제 다운로드 파일 이름
    # final_name_2 = f"lotto_{now_str}_601_1200.xls"

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



def insert_lotto_excel_to_db(excel_path):
    # MariaDB 연결
    conn = pymysql.connect(
        host='localhost',
        user='root',
        password='javakimmaster',
        db='lottodb',
        charset='utf8'
    )
    cursor = conn.cursor()


    with open(excel_path, "rb") as f:
        header = f.read(100)
        print("🔍 파일 헤더:", header[:20])


    # Excel 열기 (.xls 지원용)
    print(f"✅ 엑셀 파일 읽기 전: {excel_path}")
    book = xlrd.open_workbook(excel_path)
    sheet = book.sheet_by_index(0)
    print(f"✅ 엑셀 파일 읽기 성공: {excel_path}")

    for row in range(2, sheet.nrows):  # 0-based index, 실제 데이터는 3행부터
        draw_no = int(sheet.cell_value(row, 1))        # B열
        draw_date = xlrd.xldate.xldate_as_datetime(sheet.cell_value(row, 0), book.datemode).date()  # A열
        nums = [int(sheet.cell_value(row, col)) for col in range(13, 19)]  # N~S열 (13~18)
        bonus = int(sheet.cell_value(row, 19))          # T열 (19)

        sql = """
            INSERT IGNORE INTO lotto_result
            (draw_no, draw_date, num1, num2, num3, num4, num5, num6, bonus)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        cursor.execute(sql, (draw_no, draw_date, *nums, bonus))
    
    conn.commit()
    cursor.close()
    conn.close()
    print(f"✅ DB 저장 완료: {excel_path}")



def insert_lotto_html_to_db(file_path):
    import pandas as pd
    import pymysql
    from bs4 import BeautifulSoup
    from io import StringIO

    # 1. HTML 파일 읽기 및 파싱
    try:
        with open(file_path, 'r', encoding='euc-kr') as f:
            soup = BeautifulSoup(f, 'html.parser')
        print(f"✅ HTML 파일 열기 성공: {file_path}")
    except Exception as e:
        print(f"❌ HTML 파일 열기 실패: {e}")
        return

    # 2. 테이블 추출 및 DataFrame으로 변환
    try:
        table = soup.find_all("table")[1]  # 두 번째 테이블이 데이터 테이블
        
        # 기존 코드 수정 전
        # df = pd.read_html(str(table))[0]

        # 수정 후
        df = pd.read_html(StringIO(str(table)))[0]

        print("✅ HTML 테이블 파싱 성공")
    except Exception as e:
        print(f"❌ HTML 테이블 읽기 실패: {e}")
        return

    # ✅ 2-1. 첫 번째 행을 컬럼명으로 사용
    if df.iloc[0].str.contains("추첨일").any():  # 안전검사
        df.columns = df.iloc[0]
        df = df[1:].reset_index(drop=True)

    # 3. 컬럼명 및 데이터 정제
    try:
        df.columns = [
            "draw_year", "draw_no", "draw_date",
            "win1", "win1_amt", "win2", "win2_amt",
            "win3", "win3_amt", "win4", "win4_amt",
            "win5", "win5_amt", "num1", "num2", "num3",
            "num4", "num5", "num6", "bonus"
        ]
    except Exception as e:
        print(f"❌ 컬럼명 설정 실패: {e}")
        return

    # ✅ draw_date 제거
    df = df[["draw_no", "num1", "num2", "num3", "num4", "num5", "num6", "bonus"]]

    # 헤더로 잘못 남아있는 "회차", "추첨일", "보너스" 등의 행 제거
    df = df[df["draw_no"].str.contains("회차") == False]


    # 4. DB 연결
    try:
        conn = pymysql.connect(
            host='localhost',
            user='root',
            password='javakimmaster',
            db='lottodb',
            charset='utf8'
        )
        cursor = conn.cursor()
        print("✅ DB 연결 성공")
    except Exception as e:
        print(f"❌ DB 연결 실패: {e}")
        return

    # 5. INSERT 실행
    sql = """
        INSERT IGNORE INTO lotto_result
        (draw_no, num1, num2, num3, num4, num5, num6, bonus)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
    """
    try:
        for _, row in df.iterrows():
            cursor.execute(sql, (
                int(row['draw_no']),
                int(row['num1']), int(row['num2']), int(row['num3']),
                int(row['num4']), int(row['num5']), int(row['num6']),
                int(row['bonus'])
            ))
        conn.commit()
        print("✅ DB 저장 완료")
    except Exception as e:
        print(f"❌ INSERT 실행 중 오류: {e}")
    finally:
        cursor.close()
        conn.close()





# # 실행
# download_lotto_excel_1_600()
# time.sleep(5)

# download_lotto_excel_601_1200()
# time.sleep(5)

# driver.quit()

# time.sleep(5)

# # ✅ 예시 실행
# # insert_lotto_excel_to_db(final_name_1)

# # insert_lotto_excel_to_db(final_name_2)


# print("-----------")
# insert_lotto_html_to_db("lotto_20250723_120926_1_600.xls")

# print("-----------")
# insert_lotto_html_to_db("lotto_20250723_120926_601_1200.xls")

print("- The End -")

