from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support.ui import WebDriverWait, Select
from selenium.webdriver.support import expected_conditions as EC
import time
import os

# 크롬 옵션: 다운로드 폴더 지정
options = webdriver.ChromeOptions()
download_dir = os.getcwd()
prefs = {"download.default_directory": download_dir}
options.add_experimental_option("prefs", prefs)

# 드라이버 실행
driver = webdriver.Chrome(options=options)
driver.get("https://www.dhlottery.co.kr/gameResult.do?method=byWin")

# ✅ 전체 화면으로 전환
# driver.fullscreen_window()
driver.maximize_window()


# 명시적 대기 설정
wait = WebDriverWait(driver, 10)

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
driver.quit()


# ✅ 다운로드된 파일명 변경
# 일반적으로 다운로드되는 이름은 "번호별_출현_횟수.xlsx"임
default_name = "excel.xls"
final_name = "lotto_1_600.xls"  # 원하는 이름으로 바꾸세요

default_path = os.path.join(download_dir, default_name)
final_path = os.path.join(download_dir, final_name)

try:
    if os.path.exists(default_path):
        os.rename(default_path, final_path)
        print(f"✅ 파일 이름 변경 완료 → {final_name}")
    else:
        print(f"❌ 다운로드된 원본 파일이 존재하지 않음: {default_path}")
except Exception as e:
    print(f"❌ 파일 이름 변경 실패: {e}")
