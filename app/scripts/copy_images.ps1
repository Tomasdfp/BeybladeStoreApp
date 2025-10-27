# Copy images from web project Productos folder into app res/drawable
$src = 'C:\Users\urtub\OneDrive\Escritorio\BeyStore\BeybladeStoreReactXano-main\img\Productos'
$dst = 'app\\src\\main\\res\\drawable'
if (-not (Test-Path $src)) {
    Write-Error "Source folder not found: $src"
    exit 2
}
if (-not (Test-Path $dst)) {
    New-Item -ItemType Directory -Path $dst -Force | Out-Null
}
$copied = @()
Get-ChildItem -Path $src -File | ForEach-Object {
    $orig = $_.Name
    $nameOnly = [io.path]::GetFileNameWithoutExtension($orig).ToLower()
    $ext = $_.Extension.ToLower()
    $safe = ($nameOnly -replace '[^a-z0-9]', '_').Trim('_')
    if ([string]::IsNullOrWhiteSpace($safe)) { $safe = 'img_' + ([guid]::NewGuid().ToString().Substring(0,8)) }
    $dstPath = Join-Path $dst ($safe + $ext)
    Copy-Item -Path $_.FullName -Destination $dstPath -Force
    $copied += @{ orig = $orig; dst = [io.path]::GetFileName($dstPath) }
}
foreach ($c in $copied) { Write-Output ("Copied: $($c.orig) -> $($c.dst)") }
Write-Output ("Total copied: " + $copied.Count)
