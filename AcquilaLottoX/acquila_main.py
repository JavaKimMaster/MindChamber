from selenium import webdriver
from selenium.webdriver.common.by import By
import time
import os

# 크롬 옵션: 다운로드 폴더 지정
options = webdriver.ChromeOptions()
download_dir = os.getcwd()
prefs = {"download.default_directory": download_dir}
options.add_experimental_option("prefs", prefs)

# 크롬 드라이버 실행
driver = webdriver.Chrome(options=options)
driver.get("https://www.dhlottery.co.kr/gameResult.do?method=byWin")

# 약간 기다리기
time.sleep(2)

# 엑셀 버튼 클릭
try:
    excel_btn = driver.find_element(By.ID, "exelBtn")
    excel_btn.click()
    print("✅ 엑셀 다운로드 클릭 완료")
except Exception as e:
    print(f"❌ 버튼 클릭 실패: {e}")

# 다운로드 대기 (파일 생성될 때까지 기다림)
time.sleep(5)

driver.quit()
