$db = Get-Content 'd:\workspace\build-khanza\Khanza-2026\sik.sql' | Select-String -Pattern 'CREATE TABLE pasien' -Context 0,50
$db
