# 개인 프로젝트: 보험 상품 추천/비교 앱 (Front)
사용자가 다양한 보험 상품을 비교하고 추천받을 수 있는 안드로이드 애플리케이션입니다.
본 프로젝트는 Kotlin과 XML을 기반으로 구현되었으며, Room DB와 SharedPreferences를 활용해 사용자 정보, 최근 조회, 관심 상품 등을 안정적으로 관리할 수 있도록 설계했습니다.

<br>

## **📌 프로젝트 소개**
**"보험 상품을 쉽고 빠르게 비교·추천 받을 수 있는 안드로이드 애플리케이션”** 을 주제로 한 프론트엔드 앱입니다.
사용자는 다양한 보험 상품을 카테고리별로 탐색하고, 관심 상품을 저장하며, 맞춤형 추천을 받을 수 있습니다. 또한 Room DB와 SharedPreferences를 통해 사용자 정보와 조회 이력이 안전하게 관리되며, 채팅 기반 상담과 PDF 뷰어 기능을 통해 편리하고 신뢰성 있는 보험 선택 경험을 제공합니다.

<br>

## **⚙️ 기술 스택**
- 언어: Kotlin, XML
- DB: Room Database, SharedPreferences
- UI/UX: RecyclerView, ConstraintLayout, Vector Drawable
- 기타: Gson 기반 TypeConverter, PDF Viewer 연동

<br>

## **🚀 주요 기능**
**1. 회원가입 & 프로필 관리**
   - 다단계 회원가입 플로우 (SignUpActivity1~4)
     - ID/PW/Email 정규식 기반 유효성 검사
     - 아이디 중복 확인, 비밀번호 일치 확인
     - 직업 선택(Spinner + 기타 항목 EditText)
     - 성별, 결혼 여부, 생년월일, 전화번호 조건부 버튼 활성화
   - 프로필 수정(ProfileView)
     - 기존 직업 정보 불러오기 및 수정
     - 입력 항목별 변경 여부 및 정규식 유효성 검사 관리
     - 수정 시 안내 문구 출력, 에러 다이얼로그 표시

**2. 데이터 관리 (Room DB & SharedPreferences)**
   - Room DB 기반 단일 사용자(User) 정보 관리 (id=0 고정 저장)
   - 기존 데이터 삭제 후 신규 저장, OnConflict 전략 활용
   - WishList & RecentViewed 기능 구현
     - RecentViewedManager, WishedManager 클래스로 최근 조회/관심 상품 저장
     - SharedPreferences 기반 최근 조회 유지
   - DB Migration (User, WishList, Disease 리스트 관리)

**3. 보험 상품 조회 & 비교**
   - 상품 목록 RecyclerView
     - 카테고리(전체/암/건강/사망 등) 필터링
     - 기업명 다중 선택 필터링
     - ‘전체’ 선택 시 모든 상품 노출
   - CompareViewActivity
     - 기존 가입 상품과 신규 상품 비교
     - NumberFormatException 예외 처리

**4. 채팅 기반 AI 보험 상담**
   - RecyclerView 기반 ChatView
     - 사용자/AI 메시지 양방향 처리
     - 첫 입장 시 안내 메시지 및 빠른 질문 버튼 제공
     - 메시지 전송 시 자동 스크롤
   - 구조적 확장 고려
     - Room DB 연동하여 대화 내역 저장 및 불러오기
     - SharedPreferences 기반 최근 조회 기능 병행

**5. 기타 UI/UX 기능**
   - PDF Viewer 연동 (보험 약관/매뉴얼 확인 기능)
   - Vector 아이콘 및 색상 관리 (tint/투명도/ConstraintLayout guideline 활용)
