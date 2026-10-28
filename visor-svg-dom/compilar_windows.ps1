$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot
New-Item -ItemType Directory -Force -Path 'out' | Out-Null
$archivos = @(Get-ChildItem -Path 'src' -Filter '*.java' -Recurse | ForEach-Object { $_.FullName })
if ($archivos.Count -eq 0) { throw 'No se encontraron archivos Java.' }
& javac -encoding UTF-8 -d out @archivos
if ($LASTEXITCODE -ne 0) { throw 'Error al compilar con javac.' }
Write-Host 'Compilacion correcta. Abriendo ejemplo...'
& java -cp out visor.Main test_svg_dom.svg
