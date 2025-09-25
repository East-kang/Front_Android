# 개인 프로젝트: 보험 상품 추천/비교 앱 (Front)
사용자가 다양한 보험 상품을 **비교하고 추천받을 수 있는 안드로이드 애플리케이션**입니다.
본 프로젝트는 **Kotlin·XML**, **Room DB+SharedPreferences**를 기반으로 사용자 정보, 최근 조회, 관심 상품을 안정적으로 관리하도록 설계했습니다.

<br>

## **📌 프로젝트 소개**
**"보험 상품을 쉽고 빠르게 비교·추천 받을 수 있는 안드로이드 애플리케이션"** 을 주제로 한 프론트엔드 앱입니다.
을 주제로 한 프론트엔드 앱입니다.
사용자는 카테고리·기업 기준으로 보험 상품을 탐색하고 관심 상품을 저장하며, 맞춤형 추천 UX를 경험합니다. 또한 **로컬 영속성(Room/SharedPreferences)**, **채팅형 상담 UI**, **PDF 약관 확인(`PdfRenderer`)** 을 통해 **신뢰성 있는 선택 흐름**을 제공합니다.

<br>

## **⚙️ 기술 스택**
- **언어**: **Kotlin**, **XML**
- **DB**: **Room Database**, **SharedPreferences** (Gson `TypeConverter`로 리스트 직렬화)
- **UI/UX**: `RecyclerView`, `ConstraintLayout`, `ViewPager2` **배너**, `DrawerLayout` **내비게이션**, `Vector Drawable`, `Lottie` **애니메이션**
- **보안/인증**: **AndroidX** `BiometricPrompt`
- **문서 뷰어**: `PdfRenderer` 기반 **PDF Viewer**


<br>

## **🚀 주요 기능**
**1. 회원가입 & 프로필 관리**
   - **다단계 회원가입 플로우**(`SignUpActivity1~4`)
      - **ID/PW/Email 정규식 검사**, 아이디 중복 확인, 비밀번호 일치 확인
      - 직업 선택(`Spinner`+기타 입력), 성별·결혼 여부·생년월일·전화번호 기반 **조건부 버튼 활성화**
   - **프로필 수정**(`ProfileView.kt`)
      - 기존 직업 정보 불러오기 및 수정, **항목별 변경 여부 + 정규식 유효성** 관리
      - 안내 문구/에러 다이얼로그 처리, **지문 인증(`BiometricPrompt`)으로 민감 변경 최종 승인**

**2. 데이터 관리 (Room DB & SharedPreferences)**
   - **단일 사용자(User) 고정 저장**(`id = 0`), **OnConflict 전략**으로 안전한 갱신
   - **관심/최근 기능**
      - `WishedManager`(+ `WishedDao`/`WishedDatabase`): **찜 추가·해제·조회**, 시간순 관리
      - `RecentViewedManager`: 최근 조회 최대 N개 유지(중복 시 최신순 정렬)
   - **Gson `TypeConverter`** 로 `List<String>`(예: 질병/구독 항목) 직렬화 처리

**3. 보험 상품 조회 & 비교**
   - **목록 화면**(`RecyclerView`)
      - **카테고리 필터**(전체/암/건강/사망 등), **기업 다중 선택 필터**
      - 정렬(`Spinner`) 및 **가입 상품 제외** 옵션
   - **상세 & 비교**
      - 상세(`ProductDetail`): 찜/비교/약관(PDF) 액션, 최근 조회 반영
      - **비교 화면**(`CompareViewActivity.kt`): **기존 가입** vs **신규 상품 비교**(아이콘/기업/카테고리/추천/명칭 등)
      - 예외 처리(예: `NumberFormatException`)로 **비정상 입력 방지**

**4. 채팅 기반 AI 보험 상담**
   - `ChatView.kt`(`RecyclerView`)로 **사용자/AI 메시지 양방향 렌더링**, 첫 입장 안내·빠른 질문, **전송 시 자동 스크롤**
   - **향후 확장**: Room/서버 연동 저장 구조 고려

**5. 기타 UX 개**
   - **메인 홈 배너**(`ViewPager2`): 자동 슬라이드/옆 미리보기/트랜스폼
   - **내비게이션 드로어**(`DrawerLayout`): 프로필·가입/관심·채팅 등 주요 화면 이동
   - **PDF 약관 보기**(`PdfRenderer`+`RecyclerView`): `PdfView.kt`, `PdfPageAdapter.kt`
   - **로딩/상태 전환 애니메이션**(`Lottie`)
   - 비밀번호 보기 토글(눈 아이콘, **커서 위치 유지**), 입력 오류 **shake** 애니메이션, **뒤로가기 종료 확인** 다이얼로그 등 세부 UX
