-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: sql302.infinityfree.com
-- Generation Time: May 08, 2026 at 05:51 AM
-- Server version: 11.4.10-MariaDB
-- PHP Version: 7.2.22

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `if0_40423535_user`
--

-- --------------------------------------------------------

--
-- Table structure for table `info`
--

CREATE TABLE `info` (
  `id` int(255) NOT NULL,
  `nama` varchar(12) NOT NULL COMMENT 'username',
  `gender` varchar(6) CHARACTER SET ascii COLLATE ascii_general_ci NOT NULL,
  `tgl_lahir` date NOT NULL,
  `domisili` varchar(255) CHARACTER SET ascii COLLATE ascii_general_ci NOT NULL,
  `biodata` varchar(255) CHARACTER SET ascii COLLATE ascii_general_ci DEFAULT NULL,
  `kata_sandi` varchar(255) NOT NULL,
  `tgl_daftar` datetime NOT NULL DEFAULT utc_timestamp(),
  `sdk_int` int(4) UNSIGNED DEFAULT NULL,
  `nama_perangkat` varchar(255) CHARACTER SET ascii COLLATE ascii_general_ci DEFAULT NULL,
  `overlimit` int(11) UNSIGNED DEFAULT NULL COMMENT 'tgl overlimit login',
  `gagal_login` int(11) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=ascii COLLATE=ascii_bin;

--
-- Dumping data for table `info`
--

INSERT INTO `info` (`id`, `nama`, `gender`, `tgl_lahir`, `domisili`, `biodata`, `kata_sandi`, `tgl_daftar`, `sdk_int`, `nama_perangkat`, `overlimit`, `gagal_login`) VALUES
(26, 'budi', 'pria', '1996-04-19', 'makassar', 'halo dunia', '$2y$10$GopFgJo7Koj59g0KVZxftuSrH2B7/m9SCc9MVeVjznhylepiLXrt.', '2026-05-06 08:01:24', 35, 'Infinix X6728', NULL, 6);

-- --------------------------------------------------------

--
-- Table structure for table `interaksi`
--

CREATE TABLE `interaksi` (
  `id` int(255) NOT NULL,
  `nama` varchar(12) NOT NULL COMMENT 'username',
  `step` int(64) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'hari ke sekian',
  `step_terakhir` date DEFAULT NULL COMMENT 'kapan step terakhir',
  `total_koneksi` int(255) UNSIGNED NOT NULL DEFAULT 0,
  `koneksi_terakhir` datetime DEFAULT NULL,
  `alamat_ip_terakhir` varchar(24) DEFAULT NULL COMMENT 'alamat ip koneksi'
) ENGINE=InnoDB DEFAULT CHARSET=ascii COLLATE=ascii_bin;

--
-- Dumping data for table `interaksi`
--

INSERT INTO `interaksi` (`id`, `nama`, `step`, `step_terakhir`, `total_koneksi`, `koneksi_terakhir`, `alamat_ip_terakhir`) VALUES
(18, 'budi', 1, '2026-05-08', 7, '2026-05-08 09:47:41', '103.121.108.63');

-- --------------------------------------------------------

--
-- Table structure for table `inventori`
--

CREATE TABLE `inventori` (
  `id` int(255) NOT NULL,
  `nama` varchar(12) NOT NULL COMMENT 'username',
  `lis_item` text NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=ascii COLLATE=ascii_bin;

--
-- Dumping data for table `inventori`
--

INSERT INTO `inventori` (`id`, `nama`, `lis_item`) VALUES
(15, 'budi', '1');

-- --------------------------------------------------------

--
-- Table structure for table `kredit`
--

CREATE TABLE `kredit` (
  `id` int(255) NOT NULL,
  `nama` varchar(12) NOT NULL COMMENT 'username',
  `kredit` int(64) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'kredit saat ini',
  `kredit_keluar` int(64) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'pembelian, ...',
  `kredit_komisi` int(64) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'dari misi, referal, ...',
  `kredit_transaksi` int(64) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'dari lotre, arkade, ...',
  `kredit_promosi` int(64) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'dari peti, hadiah, ...'
) ENGINE=InnoDB DEFAULT CHARSET=ascii COLLATE=ascii_bin;

--
-- Dumping data for table `kredit`
--

INSERT INTO `kredit` (`id`, `nama`, `kredit`, `kredit_keluar`, `kredit_komisi`, `kredit_transaksi`, `kredit_promosi`) VALUES
(15, 'budi', 0, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `otentikasi`
--

CREATE TABLE `otentikasi` (
  `id` int(255) NOT NULL,
  `nama` varchar(12) NOT NULL,
  `token` varchar(64) NOT NULL,
  `waktu` datetime NOT NULL DEFAULT utc_timestamp(),
  `versi_sdk` smallint(6) NOT NULL COMMENT 'versi sdk android'
) ENGINE=InnoDB DEFAULT CHARSET=ascii COLLATE=ascii_bin;

--
-- Dumping data for table `otentikasi`
--

INSERT INTO `otentikasi` (`id`, `nama`, `token`, `waktu`, `versi_sdk`) VALUES
(33, 'budi', 'ced6360cd76b0569c490ed8fc1d114fccb0a4906', '2026-05-08 09:47:41', 0);

-- --------------------------------------------------------

--
-- Table structure for table `performa`
--

CREATE TABLE `performa` (
  `id` int(255) UNSIGNED NOT NULL,
  `nama` varchar(12) NOT NULL,
  `determinasi` int(2) UNSIGNED NOT NULL DEFAULT 50,
  `komprehensi` int(2) UNSIGNED NOT NULL DEFAULT 50,
  `akurasi` int(2) UNSIGNED NOT NULL DEFAULT 50,
  `efisiensi` int(2) UNSIGNED NOT NULL DEFAULT 50
) ENGINE=InnoDB DEFAULT CHARSET=ascii COLLATE=ascii_bin;

--
-- Dumping data for table `performa`
--

INSERT INTO `performa` (`id`, `nama`, `determinasi`, `komprehensi`, `akurasi`, `efisiensi`) VALUES
(1, 'budi', 50, 50, 50, 50);

-- --------------------------------------------------------

--
-- Table structure for table `statistik`
--

CREATE TABLE `statistik` (
  `id` int(255) UNSIGNED NOT NULL,
  `nama` varchar(12) NOT NULL COMMENT 'username',
  `total_kredit` int(64) UNSIGNED NOT NULL DEFAULT 0,
  `total_insensi` int(64) UNSIGNED NOT NULL DEFAULT 0,
  `total_konsumsi` int(64) UNSIGNED NOT NULL DEFAULT 0,
  `total_misi` int(2) UNSIGNED NOT NULL DEFAULT 0,
  `insensi_terbesar` int(2) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=ascii COLLATE=ascii_bin;

--
-- Dumping data for table `statistik`
--

INSERT INTO `statistik` (`id`, `nama`, `total_kredit`, `total_insensi`, `total_konsumsi`, `total_misi`, `insensi_terbesar`) VALUES
(1, 'budi', 0, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `status`
--

CREATE TABLE `status` (
  `id` int(255) NOT NULL,
  `nama` varchar(12) NOT NULL COMMENT 'username',
  `progres` int(64) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'total sinkron',
  `xp` smallint(24) UNSIGNED NOT NULL DEFAULT 100 COMMENT 'level = xp / 100',
  `jumlah_referi` int(64) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'total pengikut valid',
  `insentif_referal` int(64) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'kredit dari referal',
  `kartu_terpilih` varchar(1) NOT NULL DEFAULT 'D',
  `isi_peti` tinyint(3) UNSIGNED NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=ascii COLLATE=ascii_bin;

--
-- Dumping data for table `status`
--

INSERT INTO `status` (`id`, `nama`, `progres`, `xp`, `jumlah_referi`, `insentif_referal`, `kartu_terpilih`, `isi_peti`) VALUES
(15, 'budi', 0, 100, 0, 0, 'D', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `info`
--
ALTER TABLE `info`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nama` (`nama`);

--
-- Indexes for table `interaksi`
--
ALTER TABLE `interaksi`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nama` (`nama`);

--
-- Indexes for table `inventori`
--
ALTER TABLE `inventori`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_nama_4` (`nama`);

--
-- Indexes for table `kredit`
--
ALTER TABLE `kredit`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nama` (`nama`);

--
-- Indexes for table `otentikasi`
--
ALTER TABLE `otentikasi`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nama` (`nama`),
  ADD UNIQUE KEY `token` (`token`);

--
-- Indexes for table `performa`
--
ALTER TABLE `performa`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nama` (`nama`);

--
-- Indexes for table `statistik`
--
ALTER TABLE `statistik`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nama` (`nama`);

--
-- Indexes for table `status`
--
ALTER TABLE `status`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nama` (`nama`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `info`
--
ALTER TABLE `info`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `interaksi`
--
ALTER TABLE `interaksi`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `inventori`
--
ALTER TABLE `inventori`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `kredit`
--
ALTER TABLE `kredit`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `otentikasi`
--
ALTER TABLE `otentikasi`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `performa`
--
ALTER TABLE `performa`
  MODIFY `id` int(255) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `statistik`
--
ALTER TABLE `statistik`
  MODIFY `id` int(255) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `status`
--
ALTER TABLE `status`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `interaksi`
--
ALTER TABLE `interaksi`
  ADD CONSTRAINT `fk_nama_5` FOREIGN KEY (`nama`) REFERENCES `info` (`nama`) ON DELETE CASCADE;

--
-- Constraints for table `inventori`
--
ALTER TABLE `inventori`
  ADD CONSTRAINT `fk_nama_4` FOREIGN KEY (`nama`) REFERENCES `info` (`nama`) ON DELETE CASCADE;

--
-- Constraints for table `kredit`
--
ALTER TABLE `kredit`
  ADD CONSTRAINT `fk_nama_3` FOREIGN KEY (`nama`) REFERENCES `info` (`nama`) ON DELETE CASCADE;

--
-- Constraints for table `otentikasi`
--
ALTER TABLE `otentikasi`
  ADD CONSTRAINT `fk_nama_6` FOREIGN KEY (`nama`) REFERENCES `info` (`nama`) ON DELETE CASCADE;

--
-- Constraints for table `performa`
--
ALTER TABLE `performa`
  ADD CONSTRAINT `fk_nama_performa` FOREIGN KEY (`nama`) REFERENCES `info` (`nama`) ON DELETE CASCADE;

--
-- Constraints for table `statistik`
--
ALTER TABLE `statistik`
  ADD CONSTRAINT `fk_nama_statistik` FOREIGN KEY (`nama`) REFERENCES `info` (`nama`) ON DELETE CASCADE;

--
-- Constraints for table `status`
--
ALTER TABLE `status`
  ADD CONSTRAINT `fk_nama_2` FOREIGN KEY (`nama`) REFERENCES `info` (`nama`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
