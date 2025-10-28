# PowerShell script to remove comments from Kotlin/Java source files and delete vivitasol test folder
# WARNING: This modifies files in-place. Backups are created with .bak extension.

param(
    [string]$ProjectRoot = "$(Split-Path -Parent $MyInvocation.MyCommand.Definition)\..",
    [switch]$Run
)

Write-Host "Project root:" $ProjectRoot

$srcPath = Join-Path $ProjectRoot "app\src"
if (-not (Test-Path $srcPath)) {
    Write-Error "Source path not found: $srcPath"
    exit 1
}

# Find files
$files = Get-ChildItem -Path $srcPath -Include *.kt,*.java -Recurse -File
if ($files.Count -eq 0) {
    Write-Host "No .kt or .java files found under $srcPath"
}

foreach ($f in $files) {
    $full = $f.FullName
    $bak = "$full.bak"
    Write-Host "Processing: $full"
    if (-not (Test-Path $bak)) {
        Copy-Item -Path $full -Destination $bak -Force
    }
    $text = Get-Content -Raw -Encoding UTF8 -Path $full
    # Remove block comments /* ... */ (singleline regex)
    $text = [regex]::Replace($text, '/\*.*?\*/', '', [System.Text.RegularExpressions.RegexOptions]::Singleline)
    # Remove line comments //... but keep URLs (naive) - we'll remove // that are not part of http(s)://
    $text = [regex]::Replace($text, '(?m)(?<!:)//.*$', '')
    # Trim trailing whitespace on lines
    $lines = $text -split "\r?\n"
    $lines = $lines | ForEach-Object { $_.TrimEnd() }
    $new = ($lines -join "`n")
    if ($Run) {
        Set-Content -Path $full -Value $new -Encoding UTF8
        Write-Host "Updated: $full"
    } else {
        Write-Host "(dry-run) Would update: $full"
    }
}

# Remove vivitasol folder under app/src if present
$vivPath1 = Join-Path $srcPath "test\java\com\vivitasol"
$vivPath2 = Join-Path $srcPath "androidTest\java\com\vivitasol"
$removed = @()
foreach ($p in @($vivPath1, $vivPath2)) {
    if (Test-Path $p) {
        if ($Run) {
            Remove-Item -Path $p -Recurse -Force
            Write-Host "Removed folder: $p"
        } else {
            Write-Host "(dry-run) Would remove folder: $p"
        }
        $removed += $p
    }
}

Write-Host "Done. Backups (.bak) created for modified files. Use -Run to apply changes."