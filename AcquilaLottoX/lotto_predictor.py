import pandas as pd
import pymysql
from sklearn.ensemble import RandomForestClassifier
from sklearn.preprocessing import MultiLabelBinarizer
from matplotlib import pyplot as plt

# 1. 로또 DB에서 데이터 불러오기
def load_lotto_data():
    conn = pymysql.connect(
        host='localhost', user='root', password='javakimmaster',
        db='lottodb', charset='utf8', autocommit=True
    )
    query = "SELECT draw_no, num1, num2, num3, num4, num5, num6 FROM lotto_result ORDER BY draw_no"
    df = pd.read_sql(query, conn)
    conn.close()
    return df

# 2. 전처리: Feature/Label 생성
def preprocess(df):
    df['sum'] = df[['num1','num2','num3','num4','num5','num6']].sum(axis=1)
    df['range'] = df[['num1','num2','num3','num4','num5','num6']].max(axis=1) - \
                  df[['num1','num2','num3','num4','num5','num6']].min(axis=1)
    
    # 입력: 회차번호, 합계, 범위
    X = df[['draw_no', 'sum', 'range']][:-1]

    # 출력: 다음 회차의 번호(멀티라벨)
    y = df[['num1','num2','num3','num4','num5','num6']][1:]
    y_list = y.values.tolist()

    mlb = MultiLabelBinarizer(classes=range(1, 46))
    y_encoded = mlb.fit_transform(y_list)

    return X, y_encoded, mlb

# 3. 모델 학습
def train_model(X, y):
    model = RandomForestClassifier(n_estimators=200, random_state=42)
    model.fit(X, y)
    return model

# 4. 다음 회차 예측
# def predict_next_numbers(model, mlb, last_row):
#     # 기존
#     proba = model.predict_proba([last_row]) # 각 번호(1~45)에 대한 확률

#     # 수정
#     last_row_df = pd.DataFrame([last_row], columns=['draw_no', 'sum', 'range'])
#     proba = model.predict_proba(last_row_df)
#     prob_mean = [p[1] for p in proba[0]]  # 확률 리스트 추출

#     prob_sum = sum(proba)  # 확률 평균
#     prob_mean = [p[1] if isinstance(p, list) else p for p in prob_sum]
#     top6_idx = sorted(range(len(prob_mean)), key=lambda i: prob_mean[i], reverse=True)[:6]
#     predicted_numbers = [mlb.classes_[i] for i in top6_idx]
#     return predicted_numbers, prob_mean

def predict_next_numbers(model, mlb, last_row):
    # DataFrame 형태로 넣어야 경고 없음
    last_row_df = pd.DataFrame([last_row], columns=['draw_no', 'sum', 'range'])

    # 다중 출력이므로 각 클래스에 대해 확률 리스트가 따로 존재함
    # model.predict_proba → 리스트 45개 (각 번호마다)
    probas = model.predict_proba(last_row_df)

    # 각 번호(클래스)에 대해 [확률(0), 확률(1)] → 우리는 확률(1)만 필요함
    prob_mean = [proba[0][1] for proba in probas]  # [0][1] = 첫 샘플의 1일 확률

    # 확률 높은 순으로 번호 6개 선택
    top6_idx = sorted(range(len(prob_mean)), key=lambda i: prob_mean[i], reverse=True)[:6]
    predicted_numbers = [int(mlb.classes_[i]) for i in top6_idx]  # np.int64 방지

    return predicted_numbers, prob_mean



# # 5. 시각화
# def visualize(prob_mean):
#     plt.figure(figsize=(12, 5))
#     plt.bar(range(1, 46), prob_mean)
#     plt.title("번호별 당첨 확률 (RandomForest 기준)")
#     plt.xlabel("로또 번호")
#     plt.ylabel("예측 확률")
#     plt.grid(True)
#     plt.xticks(range(1, 46))
#     plt.tight_layout()
#     plt.show()



import numpy as np
def predict_multiple_sets(model, mlb, last_row, set_count=5):
    last_row_df = pd.DataFrame([last_row], columns=['draw_no', 'sum', 'range'])
    probas = model.predict_proba(last_row_df)
    prob_mean = [proba[0][1] for proba in probas]  # 클래스별 1일 확률

    number_pool = list(mlb.classes_)
    probabilities = np.array(prob_mean) / sum(prob_mean)  # 정규화

    all_sets = []
    for _ in range(set_count):
        sampled = np.random.choice(number_pool, size=6, replace=False, p=probabilities)
        all_sets.append(sorted(sampled.tolist()))
    
    return all_sets, prob_mean




# 6. 메인 실행
def main():
    df = load_lotto_data()
    X, y, mlb = preprocess(df)
    model = train_model(X, y)

    last_row = df.iloc[-1][['draw_no', 'sum', 'range']].values

    all_sets, prob_mean = predict_multiple_sets(model, mlb, last_row)
    for i, num_set in enumerate(all_sets, 1):
        print(f"🎯 추천 세트 {i}: {num_set}")

    # print(f"🎯 가장 확률 높은 조합 (상위 6개): {sorted(all_sets[0])}")


if __name__ == "__main__":
    main()
