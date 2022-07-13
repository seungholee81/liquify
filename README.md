# Liquify

liquibase changeset을 sql로 변환하는 툴이고 [https://github.com/daquino/liquify](https://github.com/daquino/liquify) 를 가져와서 내부적으로 사용하기 위해 customize 했습니다.

### Usage
1. 인텔리제이로 다운받은 liquify 프로젝트를 엽니다.
2. WEB-INF/classes/liquify_migration_to_query.xml 파일에 changeset을 넣습니다.
3. src/main/java/com.refactify/Liquify에 있는 Main 함수의 왼쪽 삼각형을 눌러 Run 시킵니다.
4. WEB-INF/classes 폴더에 생성된 liquify_migration_to_query.mysql.sql 결과물을 확인하면 됩니다.