import React, { useEffect, useRef, useState } from 'react'

import { useAtom } from 'jotai'
import { useAtomValue } from 'jotai/utils'
import Image from 'next/image'
import { TwitterPicker } from 'react-color'
import styled from 'styled-components'

import {
  isRecognitionModalOpenAtom,
  postImageElemAtom,
  postTextAtom,
  selectedBackgroundImageAtom,
  sourceTextAtom,
} from '@/atom/post'
import { PlainDivider } from '@/components/Divider'
import { Label } from '@/components/form/register/RegisterMainForm'
import LetterRecognitionModal from '@/components/modal/LetterRecognitionModal'
import { FlexDiv } from '@/components/style/div/FlexDiv'
import { FONT_SIZES, FONTS } from '@/constant/font'
import { useToggles } from '@/hook/useToggles'
import { BreakPoints } from '@/styles/breakPoint'
import { CENTER_FLEX } from '@/styles/classNames'

type Props = {}

const MainForm: React.FC<Props> = ({}) => {
  const [text, setText] = useAtom(postTextAtom)
  const [sourceText, setSourceText] = useAtom(sourceTextAtom)
  const [, setPostImageElem] = useAtom(postImageElemAtom)
  const [pickerOpen, setPickerOpen] = useState(false)
  const [textColor, setTextColor] = useState('black')
  const [isRecognitionModalOpen, setIsRecognitionModalOpen] = useAtom(isRecognitionModalOpenAtom)
  const selectedBackgroundImage = useAtomValue(selectedBackgroundImageAtom)

  const exportRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    if (exportRef?.current) setPostImageElem(exportRef.current)
  }, [exportRef?.current])

  const {
    selectedIndex: fontIndex,
    isSelectedIndex: isSelectedFontIndex,
    onToggle: onClickFont,
  } = useToggles({ defaultIndexes: [0] })
  const {
    selectedIndex: fontSizeIndex,
    isSelectedIndex: isSelectedFontSizeIndex,
    onToggle: onClickFontSize,
  } = useToggles({ defaultIndexes: [1] })

  const selectedFontFamily = FONTS[fontIndex]?.eng
  const selectedFontSize = FONT_SIZES[fontSizeIndex]?.size

  return (
    <>
      {isRecognitionModalOpen && <LetterRecognitionModal />}
      <ImageFormContainer>
        <ImageContainer
          className={'mt-10 md:mt-20'}
          ref={exportRef}
          style={{
            backgroundImage: `url("${selectedBackgroundImage.url}")`,
            backgroundPosition: 'center',
            backgroundSize: 'cover',
            backgroundRepeat: 'no-repeat',
          }}>
          <ImageSpan color={textColor} fontSize={selectedFontSize} fontFamily={selectedFontFamily}>
            {text}
          </ImageSpan>
          <SourceSpan color={'#a1a1a1'}>{sourceText}</SourceSpan>
        </ImageContainer>
        <UploadSpan
          className={'mt-2'}
          onClick={() => {
            setIsRecognitionModalOpen(true)
          }}>
          사진으로 인식해 업로드하기
        </UploadSpan>
      </ImageFormContainer>
      <div className={`${CENTER_FLEX} w-full md:w-1/2`}>
        <FormContainer>
          <Label className={'mb-2'}>쓰여질 문구</Label>
          <TextField
            onChange={(e) => {
              if (e?.target?.value.length >= 200) return setText(e.target.value.slice(0, 200))
              setText(e.target.value)
            }}
            height={'180px'}
            value={text}
          />
          <TextLimit>{text.length}/200</TextLimit>
          <Label className={'mb-2'}>원본 출처</Label>
          <TextField
            height={'52px'}
            onChange={(e) => {
              if (e?.target?.value.length >= 50) return setSourceText(e.target.value.slice(0, 50))
              setSourceText(e.target.value)
            }}
            value={sourceText}
            placeholder={'책 제목-작가 / 영화제목/ 노래 제목 - 가수'}
          />
          <TextLimit>{sourceText.length}/50</TextLimit>
          <FlexDiv width={'100%'} height={'36px'} margin={'1'} justify={'flex-start'}>
            {FONTS.map((fontInfo, index) => (
              <FontButton
                key={`FontButton_info_${index}`}
                fontSize={'14px'}
                fontFamily={fontInfo.eng}
                onClick={onClickFont(index)}
                isClicked={isSelectedFontIndex(index)}>
                {fontInfo.name}
              </FontButton>
            ))}
          </FlexDiv>
          <PlainDivider />
          <FlexDiv width={'100%'} height={'46px'} margin={'1'} justify={'flex-start'}>
            {FONT_SIZES.map((fontSizeInfo, index) => (
              <FontButton
                key={`FontButton_size_${index}`}
                fontSize={fontSizeInfo.size}
                fontFamily={selectedFontFamily}
                onClick={onClickFontSize(index)}
                isClicked={isSelectedFontSizeIndex(index)}>
                {fontSizeInfo.name}
              </FontButton>
            ))}
          </FlexDiv>
          <PlainDivider />
          <FlexDiv margin={'0'} justify={'flex-start'}>
            <FontColorButton onClick={() => setTextColor('black')}>
              글씨 색{textColor === 'black' && <Image src={'/post_check.svg'} width={20} height={20} />}
            </FontColorButton>
            <FontColorButton onClick={() => setTextColor('#fff')} color={'#fff'} bgColor={'#444444'}>
              글씨 색{textColor === '#fff' && <Image src={'/post_check_white.svg'} width={20} height={20} />}
            </FontColorButton>
            <FontColorButton
              onClick={() => setPickerOpen((pickerOpen) => !pickerOpen)}
              color={'#444444'}
              bgImage={'linear-gradient(120deg, #d4fc79 0%, #96e6a1 100%)'}>
              직접 고르기
              {textColor !== 'black' && textColor !== '#fff' && (
                <Image src={'/post_check_white.svg'} width={20} height={20} />
              )}
            </FontColorButton>
          </FlexDiv>
          <div className={`flex mt-3 ${pickerOpen ? '' : 'invisible'}`}>
            <TwitterPicker width={'340px'} color={textColor} onChangeComplete={(color) => setTextColor(color.hex)} />
          </div>
        </FormContainer>
      </div>
    </>
  )
}

const ImageFormContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  @media screen and (min-width: ${BreakPoints.md}) {
    width: 50%;
  }
`

const ImageContainer = styled.div`
  display: flex;
  width: 284px;
  height: 284px;
  border-radius: 13px;
  border: solid 1px #a1a1a1;
  position: relative;
`

type FontProps = {
  fontSize?: string
  fontFamily?: string
  color?: string
}

const ImageSpan = styled.span`
  display: flex;
  width: 100%;
  max-height: 90%;
  align-items: center;
  padding: 24px;
  white-space: pre-wrap;
  word-break: break-all;
  overflow: hidden;
  color: ${(props: FontProps) => props.color ?? 'black'};
  font-size: ${(props: FontProps) => props.fontSize};
  font-family: ${(props: FontProps) => props.fontFamily ?? 'Noto Sans KR'};
`

const SourceSpan = styled.span`
  display: flex;
  position: absolute;
  bottom: 2px;
  width: 100%;
  justify-content: center;
  align-items: center;
  white-space: pre-wrap;
  word-break: break-all;
  overflow: hidden;
  font-size: 13px;
  color: ${(props: FontProps) => props.color ?? 'black'};
`

const UploadSpan = styled.span`
  display: flex;
  width: 284px;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  text-decoration: underline;
  color: #5b5b5b;
  cursor: pointer;
`

const FormContainer = styled.div`
  display: flex;
  flex-direction: column;
  position: relative;
  width: 386px;
  margin-top: 3rem;
  @media screen and (min-width: ${BreakPoints.md}) {
    margin-top: 0rem;
  }
`

const TextField = styled.textarea<{ height: string }>`
  width: 100%;
  height: ${(props: any) => `${props.height}` ?? 'auto'};
  flex-grow: 0;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: flex-start;
  gap: 10px;
  padding: 16px;
  border-radius: 13px;
  border: solid 1px #a1a1a1;
  color: #a1a1a1;
`

const TextLimit = styled.div`
  width: 100%;
  height: 18px;
  flex-grow: 0;
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: flex-start;
  gap: 8px;
  padding: 0;
  font-size: 12px;
  color: #a1a1a1;
`
type FontColorButtonProps = {
  color?: string
  bgColor?: string
  bgImage?: string
}

const FontColorButton = styled.button`
  height: 36px;
  flex-grow: 0;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: flex-start;
  gap: 8px;
  margin: 0 8px 0 0;
  padding: 8px;
  border-radius: 13px;
  border: solid 1px #b9b9b9;
  font-size: 12px;
  background-color: ${(props: FontColorButtonProps) => props.bgColor ?? 'auto'};
  background-image: ${(props: FontColorButtonProps) => props.bgImage ?? 'none'};
  color: ${(props: FontColorButtonProps) => props.color ?? 'black'};
`

type FontButtonProps = FontProps & {
  isClicked?: boolean
}

const FontButton = styled.button`
  color: ${(props: FontButtonProps) => (props.isClicked ? '#000' : '#a1a1a1')};
  font-size: ${(props: FontButtonProps) => props.fontSize};
  font-family: ${(props: FontButtonProps) => props.fontFamily ?? 'Noto Sans KR'};
  padding: 8px;
`

export default MainForm
