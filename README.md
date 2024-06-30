# 各機能の詳細

## 従業員管理機能
- 従業員情報の登録・更新・表示・論理削除を行う
- 「社員番号」「氏名」「パスワード」「権限（一般または管理者）」を登録する
- 従業員管理機能は管理者のみが操作できる
- 同一の社員番号を複数登録することはできない

## 日報管理機能
- 日報の登録・更新・表示を行う（※削除機能はありません）
- 「日報の日付」「タイトル」「内容」「日報作成者」を登録する
- 「日報の日付」は入力がなければ操作当日の日付で登録する
- 登録された日報は、権限にかかわらず、全員が閲覧できる
- 日報の編集は、その日報を登録した従業員しか行えない

## ログイン機能
- 登録した従業員の「社員番号」と「パスワード」を使ってログインを行う

## トップページ表示機能
- ログインした従業員が作成した日報のみを一覧表示する

# 必要なテーブル

## 従業員テーブル（employees）
| カラム名 | 用途 | データ型 |
| --- | --- | --- |
| id | リソース内での連番 | 数値型 |
| code | 社員番号 | 文字列型 |
| name | 氏名 | 文字列型 |
| password | システムへのログインパスワード | 文字列型 |
| admin_flag | 管理者権限があるかどうか | 数値型（一般：0、管理者：1） |
| created_at | 登録日時 | 日時型 |
| updated_at | 更新日時 | 日時型 |
| delete_flag | 削除された従業員かどうか | 数値型（現役：0、削除済み：1） |

## 日報テーブル（reports）
| カラム名 | 用途 | データ型 |
| --- | --- | --- |
| id | リソース内での連番 | 数値型 |
| employee_id | 日報を登録した従業員の従業員テーブルでのid | 数値型 |
| report_date | いつの日報かを示す日付 | 日付型 |
| title | 日報のタイトル | 文字列型 |
| content | 日報の内容 | テキスト型 |
| created_at | 登録日時 | 日時型 |
| updated_at | 更新日時 | 日時型 |
